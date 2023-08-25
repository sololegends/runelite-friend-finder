package com.sololegends.runelite;

import java.awt.image.BufferedImage;

import com.sololegends.runelite.skills.Health;
import com.sololegends.runelite.skills.Prayer;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

public class FriendMapPoint extends WorldMapPoint {

	public static final int LIFETIME = 70_000;

	public final String friend;
	public int world;
	public boolean left_align = false;

	private Health health = new Health(0, 0);
	private Prayer prayer = new Prayer(0, 0);
	private long updated = System.currentTimeMillis();

	public FriendMapPoint(WorldPoint worldPoint, BufferedImage image, String friend, int world) {
		super(worldPoint, image);
		this.friend = friend;
		this.world = world;
	}

	public void updated() {
		updated = System.currentTimeMillis();
	}

	public boolean expired() {
		return System.currentTimeMillis() - updated > LIFETIME;
	}

	public void setPrayer(Prayer prayer) {
		this.prayer = prayer;
	}

	public void setHealth(Health health) {
		this.health = health;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public Health getHealth() {
		return health;
	}

	@Override
	public void onEdgeSnap() {
		this.setJumpOnClick(true);
	}

	@Override
	public void onEdgeUnsnap() {
		this.setJumpOnClick(false);
	}

	public FriendMapPoint asOverworld() {
		return new FriendMapPoint(
				WorldPoint.getMirrorPoint(getWorldPoint(), true),
				getImage(), friend, world);
	}

	public boolean matches(FriendMapPoint fmp) {
		WorldPoint wp = getWorldPoint();
		WorldPoint fwp = fmp.getWorldPoint();
		return wp.getX() == fwp.getX() && wp.getY() == fwp.getY();
	}

	@Override
	public int hashCode() {
		return -1;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof FriendMapPoint && ((FriendMapPoint) o).friend.equals(friend);
	}
}
