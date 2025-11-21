package com.sololegends.runelite.overlay;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.inject.Inject;

import com.sololegends.runelite.FriendsOnMapConfig;
import com.sololegends.runelite.FriendsOnMapPlugin;
import com.sololegends.runelite.helpers.WorldLocations;
import com.sololegends.runelite.helpers.WorldLocations.WorldSurface;

import net.runelite.api.Client;
import net.runelite.api.coords.*;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LineComponent;

public class PlayerLocationOverlayPanel extends OverlayPanel {

  private FriendsOnMapConfig config;
  private FriendsOnMapPlugin plugin;
  private Client client;

  @Inject
  private PlayerLocationOverlayPanel(FriendsOnMapPlugin plugin, FriendsOnMapConfig config, Client client) {
    super(plugin);
    setPosition(OverlayPosition.TOP_LEFT);
    setPriority(Overlay.PRIORITY_LOW);
    this.config = config;
    this.client = client;
    this.plugin = plugin;
  }

  @Override
  public Dimension render(Graphics2D graphics) {
    if (config.yourLocation()) {
      WorldPoint player = plugin.getPlayerLocationCache();
      LocalPoint local = client.getLocalPlayer().getLocalLocation();
      int region_id = player.getRegionID();
      if (plugin.inInstancedRegion()) {
        region_id = WorldPoint.fromLocalInstance(client, local).getRegionID();
      }
      WorldSurface s = WorldLocations.getWorldSurface(region_id);
      if (s == null || s.name.equals("Unknown")) {
        s = WorldLocations.getWorldSurface(player);
      }
      if (s == null) {
        s = new WorldSurface("Unknown", new WorldPoint(0, 0, 0), new WorldArea(0, 0, 0, 0, 0));
      }
      panelComponent.getChildren().add(LineComponent.builder()
          .left(s.name)
          .right("" + region_id)
          .build());
    }
    return super.render(graphics);
  }
}
