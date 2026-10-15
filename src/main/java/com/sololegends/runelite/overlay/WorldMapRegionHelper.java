package com.sololegends.runelite.overlay;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import com.google.inject.Inject;
import com.sololegends.runelite.FriendsOnMapConfig;
import com.sololegends.runelite.FriendsOnMapPlugin;
import com.sololegends.runelite.data.WorldRegions;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.*;

public class WorldMapRegionHelper extends Overlay {
  private static final int LABEL_PADDING = 2;
  private static final Color LABEL_BACKGROUND = new Color(255, 255, 255, 75);
  private static final Color MAP_REGION_LINE = new Color(255, 255, 255, 10);
  private static final Color MAP_REGION_TRACKED = new Color(47, 205, 68, 64);

  private final Client client;
  private final FriendsOnMapConfig config;
  private final FriendsOnMapPlugin plugin;

  @Inject
  private WorldMapRegionHelper(Client client, FriendsOnMapPlugin plugin, FriendsOnMapConfig config) {
    this.client = client;
    this.config = config;
    this.plugin = plugin;
    setPosition(OverlayPosition.DYNAMIC);
    setPriority(Overlay.PRIORITY_HIGH);
    setLayer(OverlayLayer.MANUAL);
    drawAfterLayer(InterfaceID.Worldmap.MAP_CONTAINER);
  }

  @Override
  public Dimension render(Graphics2D graphics) {
    if (config.mapDrawRegions()) {
      drawRegions(graphics);
    }

    return null;
  }

  private void drawRegions(Graphics2D graphics) {
    final int grid_size = 64;
    final int truncate = ~(grid_size - 1);

    Widget map = client.getWidget(InterfaceID.Worldmap.MAP_CONTAINER);
    float map_zoom = client.getWorldMap().getWorldMapZoom();

    if (map == null || grid_size * map_zoom < 3) {
      return;
    }

    Rectangle map_rect = map.getBounds();
    graphics.setClip(map_rect);

    int tile_width = (int) Math.ceil(map_rect.getWidth() / map_zoom);
    int tile_height = (int) Math.ceil(map_rect.getHeight() / map_zoom);

    Point map_pos = client.getWorldMap().getWorldMapPosition();

    int tile_y_min = map_pos.getY() - tile_height / 2;
    int region_x_min = (map_pos.getX() - tile_width / 2) & truncate;
    int region_x_max = ((map_pos.getX() + tile_width / 2) & truncate) + grid_size;
    int region_y_min = (tile_y_min & truncate);
    int region_y_max = ((map_pos.getY() + tile_height / 2) & truncate) + grid_size;
    int region_size = (int) Math.ceil(grid_size * map_zoom);

    Point mouse = client.getMouseCanvasPosition();
    if (mouse == null) {
      mouse = new Point(0, 0);
    }
    for (int x = region_x_min; x < region_x_max; x += grid_size) {
      for (int y = region_y_min; y < region_y_max; y += grid_size) {
        int tile_offset_y = -(tile_y_min - y);
        int tile_offset_x = x + tile_width / 2 - map_pos.getX();

        int pos_x = ((int) (tile_offset_x * map_zoom)) + (int) map_rect.getX();
        int pos_y = (map_rect.height - (int) (tile_offset_y * map_zoom)) + (int) map_rect.getY();
        pos_y -= region_size;

        graphics.setColor(MAP_REGION_LINE);

        int region_id = ((x >> 6) << 8) | (y >> 6);
        // Draw as complete or not
        graphics.drawRect(pos_x, pos_y, region_size, region_size);
        if (WorldRegions.hasRegion(region_id)) {
          graphics.setColor(MAP_REGION_TRACKED);
          graphics.fillRect(pos_x + 1, pos_y + 1, region_size - 2, region_size - 2);
        }

        graphics.setColor(LABEL_BACKGROUND);

        String region_label = String.valueOf(region_id);
        FontMetrics fm = graphics.getFontMetrics();
        Rectangle2D label_bounds = fm.getStringBounds(region_label, graphics);
        int label_width = (int) label_bounds.getWidth() + 2 * LABEL_PADDING;
        int label_height = (int) label_bounds.getHeight() + 2 * LABEL_PADDING;
        graphics.fillRect(pos_x, pos_y, label_width, label_height);
        graphics.setColor(Color.BLACK);
        graphics.drawString(
            region_label,
            pos_x + LABEL_PADDING,
            pos_y + (int) label_bounds.getHeight() + LABEL_PADDING);

        if (WorldRegions.hasRegion(region_id) && mouse.getX() > pos_x && mouse.getX() < pos_x + label_width
            && mouse.getY() > pos_y && mouse.getY() < pos_y + label_height) {
          plugin.drawToolTip(
              graphics,
              WorldRegions.fromRegion(region_id).name,
              mouse.getX(),
              20 + mouse.getY(),
              region_id);
        }
      }
    }
  }
}