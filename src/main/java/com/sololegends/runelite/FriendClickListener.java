package com.sololegends.runelite;

import java.awt.event.MouseEvent;

import javax.inject.Inject;

import net.runelite.client.input.MouseListener;

public class FriendClickListener implements MouseListener {

  private final FriendsOnMapPlugin plugin;
  private final FriendsOnMapConfig config;

  @Inject
  private FriendClickListener(FriendsOnMapPlugin plugin, FriendsOnMapConfig config) {
    this.plugin = plugin;
    this.config = config;
  }

  @Override
  public MouseEvent mouseClicked(MouseEvent mouseEvent) {
    if (config.dblClickWorldHop() && mouseEvent.getButton() == MouseEvent.BUTTON1
        && mouseEvent.getClickCount() == 2) {
      plugin.worldSwitchClick();
      return mouseEvent;
    }
    return mouseEvent;
  }

  @Override
  public MouseEvent mousePressed(MouseEvent mouseEvent) {
    return mouseEvent;
  }

  @Override
  public MouseEvent mouseReleased(MouseEvent mouseEvent) {
    return mouseEvent;
  }

  @Override
  public MouseEvent mouseEntered(MouseEvent mouseEvent) {
    return mouseEvent;
  }

  @Override
  public MouseEvent mouseExited(MouseEvent mouseEvent) {
    return mouseEvent;
  }

  @Override
  public MouseEvent mouseDragged(MouseEvent mouseEvent) {
    return mouseEvent;
  }

  @Override
  public MouseEvent mouseMoved(MouseEvent mouseEvent) {
    return mouseEvent;
  }

}
