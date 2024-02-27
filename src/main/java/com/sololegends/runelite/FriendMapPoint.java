package com.sololegends.runelite;

import java.awt.image.BufferedImage;

import com.sololegends.runelite.helpers.WorldLocations;
import com.sololegends.runelite.helpers.WorldLocations.WorldSurface;
import com.sololegends.runelite.skills.Health;
import com.sololegends.runelite.skills.Prayer;

import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

public class FriendMapPoint extends WorldMapPoint {

  public static final int LIFETIME = 70_000;

  public final String friend;
  public int world;
  public int region = -1;
  public boolean left_align = false;

  private Health health = new Health(0, 0);
  private Prayer prayer = new Prayer(0, 0);
  private long updated = System.currentTimeMillis();
  private int point_offset = -1;
  private WorldSurface location = null;

  public FriendMapPoint(WorldPoint worldPoint, BufferedImage image, String friend, int world) {
    super(worldPoint, image);
    this.friend = friend;
    this.world = world;
    point_offset = image.getHeight() / 2;
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

  public void setLocation(WorldSurface location) {
    this.location = location;
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

  public void setRegion(int region) {
    this.region = region;
  }

  public int getRegion() {
    return region;
  }

  @Override
  public void onEdgeSnap() {
    super.onEdgeSnap();
    this.setJumpOnClick(true);

    // Setup the display of this icon
    setImagePoint(null);
  }

  @Override
  public void onEdgeUnsnap() {
    super.onEdgeUnsnap();
    this.setJumpOnClick(false);

    // Setup the display of this icon
    setImagePoint(new Point(point_offset, point_offset));
  }

  @Override
  public void setImage(BufferedImage image) {
    super.setImage(image);
    point_offset = image.getHeight() / 2;
    // Update the point
    if (!isCurrentlyEdgeSnapped()) {
      setImagePoint(new Point(point_offset, point_offset));
    }
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

  public WorldSurface getLocation() {
    WorldSurface sur = null;
    if (location != null) {
      sur = location;
    }
    if (getRegion() != -1) {
      sur = WorldLocations.getWorldSurface(getRegion());
    }
    if (sur == null) {
      sur = WorldLocations.getWorldSurface(getWorldPoint());
      ;
    }
    return sur;
  }

  @Override
  public int hashCode() {
    return -1;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof FriendMapPoint && ((FriendMapPoint) o).friend.equalsIgnoreCase(friend);
  }
}
