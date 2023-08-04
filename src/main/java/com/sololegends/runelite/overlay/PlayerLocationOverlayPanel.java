package com.sololegends.runelite.overlay;

import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.inject.Inject;

import com.sololegends.runelite.FriendsOnMapConfig;
import com.sololegends.runelite.FriendsOnMapPlugin;
import com.sololegends.runelite.helpers.WorldLocations;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LineComponent;

public class PlayerLocationOverlayPanel extends OverlayPanel {

	private FriendsOnMapConfig config;

	@Inject
	private WorldLocations locations;

	@Inject
	private Client client;

	@Inject
	private PlayerLocationOverlayPanel(FriendsOnMapPlugin plugin, FriendsOnMapConfig config) {
		super(plugin);
		setPosition(OverlayPosition.TOP_LEFT);
		setPriority(OverlayPriority.LOW);
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		if (config.yourLocation()) {
			WorldPoint player = client.getLocalPlayer().getWorldLocation();
			panelComponent.getChildren().add(LineComponent.builder()
					.left(locations.getWorldSurface(player).name)
					.build());
		}
		return super.render(graphics);
	}
}
