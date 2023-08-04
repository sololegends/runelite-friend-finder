package com.sololegends.runelite;

import java.awt.image.BufferedImage;

import com.sololegends.runelite.skills.Health;
import com.sololegends.runelite.skills.Prayer;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

public class FriendMapPoint extends WorldMapPoint {

	public final String friend;
	public final int world;

	private Health health = new Health(0, 0);
	private Prayer prayer = new Prayer(0, 0);

	public FriendMapPoint(WorldPoint worldPoint, BufferedImage image, String friend, int world) {
		super(worldPoint, image);
		this.friend = friend;
		this.world = world;
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
}
