package com.sololegends.runelite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import com.google.inject.Provides;
import com.sololegends.parsers.json.JSONCParser;
import com.sololegends.utilities.JSON;
import com.sololegends.utilities.JSONType;
import com.sololegends.utilities.web.HTTPRequest;
import com.sololegends.utilities.web.HTTPResponse;
import com.sololegends.utilities.web.RequestParameters.JSONRequestParameters;
import com.sololegends.utilities.web.enums.HTTPMethod;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;

@Slf4j
@PluginDescriptor(name = "friend-finder")
public class FriendsOnMapPlugin extends Plugin {

	private long last_update = 0;
	private final long update_interval = 2_000;

	@Inject
	private Client client;

	@Inject
	private FriendsOnMapConfig config;

	@Inject
	private WorldMapPointManager map_point_manager;

	@Override
	protected void startUp() throws Exception {
		log.info("Starting Friend finder");
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("Stopping Friend finder!");
	}

	private BufferedImage getIcon() {
		BufferedImage icon = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) icon.getGraphics();
		g.setColor(Color.GREEN);
		g.fillOval(1, 1, 48, 48);
		return icon;
	}

	@Subscribe
	public void onClientTick() {
		if (System.currentTimeMillis() - last_update > update_interval) {
			// Send player info to server
			Player player = client.getLocalPlayer();
			if (player == null) {
				// Set updated time
				last_update = System.currentTimeMillis();
				return;
			}
			WorldPoint player_location = player.getWorldLocation();
			Friend[] friends = client.getFriendContainer().getMembers();
			// Build the payload
			JSON payload = new JSON();
			payload.set("name", player.getName());
			payload.set("x", player_location.getX());
			payload.set("y", player_location.getY());
			payload.set("z", player_location.getPlane());
			payload.set("w", client.getWorld());
			JSON friends_json = new JSON(JSONType.LIST);
			if (player_location != null) {
				for (Friend f : friends) {
					friends_json.add(f.getName());
				}
			}
			payload.set("friends", friends_json);

			// Retrieve friends info from server
			try {
				HTTPRequest req = new HTTPRequest(new URL(config.friendsAPI()));
				HTTPResponse resp = req.call(new JSONRequestParameters(payload), HTTPMethod.GET);
				if (resp.code() != 200) {
					client.addChatMessage(ChatMessageType.CONSOLE, "friends-error",
							"Failed to retrieve friends from friends api [RC2]",
							"Friend Finder Plugin", true);
					// Set updated time
					last_update = System.currentTimeMillis();
					return;
				}
				JSON my_friends = JSONCParser.parse(resp.data());
				if (my_friends != null && my_friends.type().isList()) {
					for (JSON f : my_friends.list()) {
						// Validate the friend object
						if (f.contains("x") && f.contains("y") && f.contains("z") && f.contains("name") && f.contains("w")) {
							WorldMapPoint wmp = new WorldMapPoint(
									new WorldPoint(f.get("x").int32(), f.get("y").int32(), f.get("z").int32()),
									getIcon());
							wmp.setName(f.get("name").string());
							wmp.setTooltip("World: " + f.get("w").string());
							map_point_manager.remove(wmp);
							map_point_manager.add(wmp);
						}
					}
				}
			} catch (MalformedURLException e) {
				client.addChatMessage(ChatMessageType.CONSOLE, "friends-error",
						"Failed to retrieve friends from friends api [MUE]",
						"Friend Finder Plugin", true);
			} catch (IOException e) {
				client.addChatMessage(ChatMessageType.CONSOLE, "friends-error",
						"Failed to retrieve friends from friends api [IOE]",
						"Friend Finder Plugin", true);
			}
			// Set updated time
			last_update = System.currentTimeMillis();
		}
	}

	@Provides
	FriendsOnMapConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(FriendsOnMapConfig.class);
	}
}
