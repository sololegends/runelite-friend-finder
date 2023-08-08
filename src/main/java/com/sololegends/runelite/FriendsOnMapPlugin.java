package com.sololegends.runelite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Provides;
import com.sololegends.runelite.helpers.RemoteDataManager;
import com.sololegends.runelite.overlay.OtherSurfacePlayersOverlay;
import com.sololegends.runelite.overlay.PlayerLocationOverlayPanel;
import com.sololegends.runelite.panel.FriendsPanel;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.worldmap.WorldMap;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.worldmap.*;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.World;

@Slf4j
@PluginDescriptor(name = "Friend Finder")
public class FriendsOnMapPlugin extends Plugin {

	private HashSet<FriendMapPoint> current_points = new HashSet<>();
	private LinkedBlockingQueue<String> message_queue = new LinkedBlockingQueue<>();

	private long last_update = 0;
	private net.runelite.api.World hop_world = null;
	private int hop_world_attempts = 0;
	private int hop_world_attempts_max = 10;
	private long hop_world_last = 0;
	private int hop_world_interval = 1_000;

	// Player information
	private volatile boolean info_updated = false;
	private int health_last;
	private int prayer_last;

	// UI Elements
	private FriendsPanel panel;
	private NavigationButton side_panel_btn;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private FriendsOnMapConfig config;

	@Inject
	private WorldMapPointManager map_point_manager;

	@Inject
	private WorldMapOverlay map_overlay;

	@Inject
	private MouseManager mouse;

	@Inject
	private FriendClickListener mouse_listener;

	@Inject
	private WorldService worldService;

	@Inject
	private RemoteDataManager remote;

	@Inject
	private OtherSurfacePlayersOverlay other_surface_overlay;

	@Inject
	private PlayerLocationOverlayPanel player_location_overlay;

	@Inject
	private OverlayManager overlay_manager;

	@Override
	protected void startUp() throws Exception {
		log.info("Starting Friend finder");
		mouse.registerMouseListener(mouse_listener);
		overlay_manager.add(other_surface_overlay);
		overlay_manager.add(player_location_overlay);

		panel = injector.getInstance(FriendsPanel.class);

		final BufferedImage icon = ImageUtil.loadImageResource(FriendsOnMapPlugin.class, "panel_icon_sm.png");

		side_panel_btn = NavigationButton.builder()
				.tooltip("Friends On Map")
				.icon(icon)
				.priority(9)
				.panel(panel)
				.build();

		// TODO: Enable when ready
		clientToolbar.addNavigation(side_panel_btn);
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("Stopping Friend finder!");
		clientToolbar.removeNavigation(side_panel_btn);
		mouse.unregisterMouseListener(mouse_listener);
		overlay_manager.remove(other_surface_overlay);
		overlay_manager.remove(player_location_overlay);
	}

	public void message(String msg) {
		message_queue.add(msg);
	}

	public void updated(long time) {
		last_update = time;
	}

	public void clearPoints() {
		for (WorldMapPoint p : current_points) {
			map_point_manager.remove(p);
		}
		current_points.clear();
	}

	public void addPoint(FriendMapPoint fmp) {
		map_point_manager.add(fmp);
		current_points.add(fmp);
	}

	public void updatePanel() {
		SwingUtilities.invokeLater(() -> panel.update());
	}

	public boolean isCurrentWorld(int world) {
		return client.getWorld() == world;
	}

	public Set<FriendMapPoint> currentPoints() {
		return current_points;
	}

	public void focusFriendClick() {
		if (isMouseInWorldMap()) {
			final Point mouse_pos = client.getMouseCanvasPosition();
			for (FriendMapPoint point : current_points) {
				if (isWithin(mouse_pos, point.getWorldPoint()) && point.isCurrentlyEdgeSnapped()) {

				}
			}
		}
	}

	public void worldSwitchClick() {
		if (isMouseInWorldMap()) {
			final Point mouse_pos = client.getMouseCanvasPosition();
			for (FriendMapPoint point : current_points) {
				if (isWithin(mouse_pos, point.getWorldPoint())) {
					message_queue.add(
							new ChatMessageBuilder()
									.append("Attempting to switch to ")
									.append(ChatColorType.HIGHLIGHT)
									.append(point.friend)
									.append(ChatColorType.NORMAL)
									.append("'s world ")
									.append(ChatColorType.HIGHLIGHT)
									.append(Integer.toString(point.world))
									.append(ChatColorType.NORMAL).build());
					hopToWorld(point.world);
				}
			}
		}
	}

	private boolean isWithin(Point mouse_pos, WorldPoint point) {
		final WorldMap map = client.getWorldMap();
		final float zoom = map.getWorldMapZoom();
		final WorldPoint map_click = calculateMapPoint(map, mouse_pos, zoom);
		return point.distanceTo2D(map_click) < config.dotSize();
	}

	private WorldPoint calculateMapPoint(WorldMap map, Point mousePos, float zoom) {
		final WorldPoint map_point = new WorldPoint(map.getWorldMapPosition().getX(),
				map.getWorldMapPosition().getY(), 0);
		final Point middle = map_overlay.mapWorldPointToGraphicsPoint(map_point);

		final int dx = (int) ((mousePos.getX() - middle.getX()) / zoom);
		final int dy = (int) ((-(mousePos.getY() - middle.getY())) / zoom);

		return map_point.dx(dx).dy(dy);
	}

	private boolean isMouseInWorldMap() {
		final Point mousePos = client.getMouseCanvasPosition();
		final Widget view = client.getWidget(WidgetInfo.WORLD_MAP_VIEW);
		if (view == null) {
			return false;
		}

		final Rectangle worldMapBounds = view.getBounds();
		return worldMapBounds.contains(mousePos.getX(), mousePos.getY());
	}

	public void hopToWorld(int world_id) {
		World world = worldService.getWorlds().findWorld(world_id);
		if (world == null || client.getWorld() == world_id) {
			message_queue.add(
					new ChatMessageBuilder()
							.append("Failed to load world ")
							.append(ChatColorType.HIGHLIGHT)
							.append(Integer.toString(world_id))
							.append(ChatColorType.NORMAL).build());
			return;
		}
		clientThread.invoke(() -> hop(world));
	}

	public void hop(World world) {
		assert client.isClientThread();
		final net.runelite.api.World rsWorld = client.createWorld();
		rsWorld.setActivity(world.getActivity());
		rsWorld.setAddress(world.getAddress());
		rsWorld.setId(world.getId());
		rsWorld.setPlayerCount(world.getPlayers());
		rsWorld.setLocation(world.getLocation());
		rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));
		hop_world = rsWorld;
		hop_world_attempts = 0;
	}

	public BufferedImage getIcon(boolean off_world, boolean translated) {
		int d_size = config.dotSize();
		BufferedImage icon = new BufferedImage(d_size + 2, d_size + 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) icon.getGraphics();
		drawIcon(off_world, translated, g, 1, 1);
		return icon;
	}

	public Dimension drawIcon(boolean off_world, boolean translated, Graphics2D g, int x, int y) {
		int d_size = config.dotSize();
		Color oc = g.getColor();
		Color owc = translated ? config.otherWorldColorLink() : config.otherWorldColor();
		Color wc = translated ? config.dotColorLink() : config.dotColor();

		g.setColor(off_world && !config.offWorldAsOutline() ? owc : wc);
		g.fillOval(x, y, d_size, d_size);
		if (off_world && config.offWorldAsOutline()) {
			g.setColor(owc);
			g.setStroke(new BasicStroke(Math.max(2, config.outlineSize())));
			g.drawOval(x, y, d_size, d_size);
		}
		g.setColor(oc);
		return new Dimension(d_size, d_size);
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		String msg = null;
		while ((msg = message_queue.poll()) != null) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "friend-finder",
					msg, "Friend Finder Plugin", true);
		}
		if (hop_world != null && hop_world_attempts < hop_world_attempts_max
				&& System.currentTimeMillis() - hop_world_last > hop_world_interval) {
			hop_world_last = System.currentTimeMillis();
			hop_world_attempts++;
			if (client.getWorld() != hop_world.getId()) {
				client.hopToWorld(hop_world);
			} else {
				hop_world = null;
			}
		} else if (hop_world != null) {
			hop_world = null;
		}
		if (System.currentTimeMillis() - last_update > config.updateInterval().interval() || info_updated) {
			info_updated = false;
			// USE FRIENDS API
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
			JsonObject payload = new JsonObject();
			payload.addProperty("name", player.getName());
			payload.addProperty("id", player.getId());
			payload.addProperty("x", player_location.getX());
			payload.addProperty("hm", client.getBoostedSkillLevel(Skill.HITPOINTS));
			payload.addProperty("hM", client.getRealSkillLevel(Skill.HITPOINTS));
			payload.addProperty("pm", client.getBoostedSkillLevel(Skill.PRAYER));
			payload.addProperty("pM", client.getRealSkillLevel(Skill.PRAYER));
			payload.addProperty("y", player_location.getY());
			payload.addProperty("z", player_location.getPlane());

			payload.addProperty("w", client.getWorld());
			JsonArray friends_json = new JsonArray();
			if (player_location != null) {
				for (Friend f : friends) {
					friends_json.add(f.getName());
				}
			}
			payload.add("friends", friends_json);

			// Retrieve friends info from server
			remote.sendRequest(payload);
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged) {
		Skill skill = statChanged.getSkill();
		int current_value = client.getBoostedSkillLevel(skill);
		switch (skill) {
			case AGILITY:
			case ATTACK:
			case CONSTRUCTION:
			case COOKING:
			case CRAFTING:
			case DEFENCE:
			case FARMING:
			case FIREMAKING:
			case FISHING:
			case FLETCHING:
			case HERBLORE:
			case HUNTER:
			case MAGIC:
			case MINING:
			case RANGED:
			case RUNECRAFT:
			case SLAYER:
			case SMITHING:
			case STRENGTH:
			case THIEVING:
			case WOODCUTTING:
				break;
			case HITPOINTS:
				if (health_last != current_value) {
					info_updated = true;
					health_last = current_value;
				}
				break;
			case PRAYER:
				if (prayer_last != current_value) {
					info_updated = true;
					prayer_last = current_value;
				}
				break;
			default:
				break;
		}
	}

	@Provides
	FriendsOnMapConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(FriendsOnMapConfig.class);
	}
}
