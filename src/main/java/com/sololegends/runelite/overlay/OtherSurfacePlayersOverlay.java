package com.sololegends.runelite.overlay;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.*;

import com.google.inject.Inject;
import com.sololegends.runelite.*;
import com.sololegends.runelite.helpers.WorldLocations;
import com.sololegends.runelite.helpers.WorldLocations.WorldSurface;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.worldmap.WorldMap;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;

public class OtherSurfacePlayersOverlay extends Overlay {

  private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);
  private final Client client;
  private final FriendsOnMapPlugin plugin;
  private final FriendsOnMapConfig config;

  @Inject
  private WorldMapOverlay map_overlay;

  @Inject
  private OtherSurfacePlayersOverlay(Client client, FriendsOnMapPlugin plugin, FriendsOnMapConfig config) {
    super(plugin);
    this.client = client;
    this.plugin = plugin;
    this.config = config;
    setPosition(OverlayPosition.DYNAMIC);
    setPriority(Overlay.PRIORITY_HIGHEST);
    setLayer(OverlayLayer.ABOVE_WIDGETS);
    drawAfterLayer(InterfaceID.Worldmap.MAP_CONTAINER);
  }

  @Override
  public Dimension render(Graphics2D g) {
    Widget parent = client.getWidget(InterfaceID.Worldmap.MAP_CONTAINER);
    if (parent == null || parent.isHidden()) {
      return null;
    }
    Point mouse = client.getMouseCanvasPosition();

    Rectangle bounds = parent.getBounds();
    Shape clip = getWorldMapClipArea(bounds);
    g.setClip(clip);

    int y_offset = 10, x = 0, y = 0;
    WorldMap worldMap = client.getWorldMap();

    String draw_tip = null;
    int draw_tip_region = -1;

    ArrayList<FriendMapPoint> points = new ArrayList<>(plugin.currentPoints());
    points.sort(new Comparator<FriendMapPoint>() {
      @Override
      public int compare(FriendMapPoint o1, FriendMapPoint o2) {
        return o1.friend.compareTo(o2.friend);
      }
    });
    for (FriendMapPoint f : points) {
      if (worldMap.getWorldMapData().surfaceContainsPosition(f.getWorldPoint().getX(), f.getWorldPoint().getY())) {
        continue;
      }
      x = bounds.x + 10;
      y = bounds.y + y_offset;
      // Not in the same world surface
      Dimension dim = drawLocationGraphic(g, f, x, y);
      // If mouse is hovering over a friend chip
      if (mouse.getX() > x && mouse.getX() < x + dim.width
          && mouse.getY() > y && mouse.getY() < y + dim.height) {
        // Draw tool tip stating world surface
        WorldSurface loc = f.getLocation();
        draw_tip = loc.name;
        draw_tip_region = f.region;
      }
      y_offset += dim.getHeight() + 5;
    }

    if (config.mapDrawAreas()) {
      drawAreas(g, worldMap);
    }
    // Render tool tip last so it is on top
    if (draw_tip != null) {
      plugin.drawToolTip(g, draw_tip, mouse.getX(), 20 + mouse.getY(), draw_tip_region);
    }
    return null;
  }

  private void drawAreas(Graphics2D g, WorldMap map) {
    Set<WorldSurface> surfaces = WorldLocations.getSurfaces();
    Widget map_w = client.getWidget(InterfaceID.Worldmap.MAP_CONTAINER);

    if (map_w == null) {
      return;
    }

    g.setFont(FontManager.getRunescapeFont().deriveFont(Font.PLAIN, 13));
    FontMetrics fm = g.getFontMetrics();
    for (WorldSurface s : surfaces) {
      for (WorldArea a : s.areas) {
        WorldPoint start = new WorldPoint(a.getX(), a.getY(), 0);
        WorldPoint end = new WorldPoint(a.getX() + a.getWidth(), a.getY() + a.getHeight(), 0);
        Point draw_start = map_overlay.mapWorldPointToGraphicsPoint(start);
        Point draw_end = map_overlay.mapWorldPointToGraphicsPoint(end);

        if (draw_start == null || draw_end == null) {
          continue;
        }
        g.setColor(Color.RED);
        g.drawRect(draw_start.getX(), draw_end.getY(), draw_end.getX() - draw_start.getX(),
            draw_start.getY() - draw_end.getY());
        Rectangle2D bounds = fm.getStringBounds(s.name, g);
        g.setColor(Color.BLACK);
        g.drawString(s.name, draw_start.getX() + 4, draw_end.getY() + 2 + (int) bounds.getHeight());
        g.setColor(Color.WHITE);
        g.drawString(s.name, draw_start.getX() + 3, draw_end.getY() + 3 + (int) bounds.getHeight());
      }
    }
  }

  private Dimension drawLocationGraphic(Graphics2D g, FriendMapPoint f, int x_offset, int y_offset) {
    g.setFont(FONT);
    FontMetrics fm = g.getFontMetrics();
    Rectangle2D bounds = fm.getStringBounds(f.getName(), g);

    Dimension dim = plugin.drawIcon(f.world != client.getWorld(), true, g, x_offset, y_offset);

    // Drop shadow
    g.setColor(Color.BLACK);
    g.drawString(f.getName(), x_offset + 6 + dim.width, 1 + y_offset + (int) bounds.getHeight());

    g.setColor(Color.WHITE);
    g.drawString(f.getName(), x_offset + 5 + dim.width, y_offset + (int) bounds.getHeight());

    return new Dimension(dim.width + (int) bounds.getWidth() + 5, (int) bounds.getHeight());
  }

  /**
   * Gets a clip area which excludes the area of widgets which overlay the world
   * map.
   *
   * @param baseRectangle The base area to clip from
   * @return An {@link Area} representing <code>baseRectangle</code>, with the
   *         area
   *         of visible widgets overlaying the world map clipped from it.
   */
  private Shape getWorldMapClipArea(Rectangle baseRectangle) {
    final Widget overview = client.getWidget(InterfaceID.Worldmap.OVERVIEW_CONTAINER);
    final Widget surfaceSelector = client.getWidget(InterfaceID.Worldmap.MAPLIST_BOX);

    Area clipArea = new Area(baseRectangle);
    boolean subtracted = false;

    if (overview != null && !overview.isHidden()) {
      clipArea.subtract(new Area(overview.getBounds()));
      subtracted = true;
    }

    if (surfaceSelector != null && !surfaceSelector.isHidden()) {
      clipArea.subtract(new Area(surfaceSelector.getBounds()));
      subtracted = true;
    }

    // The sun g2d implementation is much more efficient at applying clips which are
    // subclasses of rectangle2d,
    // so use that as the clip shape if possible
    return subtracted ? clipArea : baseRectangle;
  }
}
