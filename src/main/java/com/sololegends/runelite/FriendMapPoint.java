package com.sololegends.runelite;

import java.awt.image.BufferedImage;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

public class FriendMapPoint extends WorldMapPoint {

	public final String friend;
	public final int world;

	public FriendMapPoint(WorldPoint worldPoint, BufferedImage image, String friend, int world) {
		super(worldPoint, image);
		this.friend = friend;
		this.world = world;
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
