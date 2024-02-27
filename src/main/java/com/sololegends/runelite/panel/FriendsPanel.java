package com.sololegends.runelite.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.inject.Inject;
import com.sololegends.runelite.*;

import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.DragAndDropReorderPane;
import net.runelite.client.util.LinkBrowser;

public class FriendsPanel extends PluginPanel {

  private final FriendsOnMapPlugin plugin;

  private final JComponent friends = new DragAndDropReorderPane();
  private final Map<String, Component> friend_components = new ConcurrentHashMap<>();

  private final ImageIcon health_icon;
  private final ImageIcon prayer_icon;

  @Inject
  FriendsPanel(final FriendsOnMapPlugin plugin, final FriendsOnMapConfig config, final SkillIconManager icon_manager) {
    this.plugin = plugin;
    health_icon = new ImageIcon(icon_manager.getSkillImage(Skill.HITPOINTS, true));
    prayer_icon = new ImageIcon(icon_manager.getSkillImage(Skill.PRAYER, true));

    setBorder(new EmptyBorder(10, 10, 10, 10));
    setBackground(ColorScheme.DARK_GRAY_COLOR);
    setLayout(new BorderLayout());

    final JPanel layout = new JPanel();
    BoxLayout boxLayout = new BoxLayout(layout, BoxLayout.Y_AXIS);
    layout.setLayout(boxLayout);
    add(layout, BorderLayout.CENTER);

    // Missing location report panel
    final JPanel report_panel = new JPanel();
    if (config.reportLink() != null && !config.reportLink().isBlank()) {
      report_panel.setBorder(new EmptyBorder(0, 0, 4, 0));
      final JButton submit = new JButton("Report Missing Location");
      submit.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          LinkBrowser.browse(config.reportLink());
        }
      });
      report_panel.add(submit);
    }

    // Info Panel (just header atm)
    final JPanel info_panel = new JPanel();
    info_panel.setBorder(new EmptyBorder(0, 0, 4, 0));
    info_panel.add(new JLabel("Friends Info"));

    layout.add(report_panel);
    layout.add(Box.createRigidArea(new Dimension(0, 3)));
    layout.add(getJSeparator(ColorScheme.LIGHT_GRAY_COLOR));
    layout.add(info_panel);
    layout.add(friends);

    layout.revalidate();
    layout.repaint();
  }

  public void clear() {
    friends.removeAll();
    friends.revalidate();
  }

  public void prune() {
    Set<String> panels = new HashSet<>(friend_components.keySet());
    for (String s : panels) {
      FriendPanel panel = (FriendPanel) friend_components.get(s);
      if (panel.expired()) {
        friends.remove(friend_components.remove(s));
      }
    }
  }

  public void update() {
    Set<FriendMapPoint> friend_points = plugin.currentPoints();
    for (FriendMapPoint friend : friend_points) {
      // Update
      if (friend_components.containsKey(friend.friend)) {
        FriendPanel panel = (FriendPanel) friend_components.get(friend.friend);
        panel.update(friend, friend.getLocation());
        continue;
      }
      FriendPanel panel = new FriendPanel(this, friend, health_icon, prayer_icon);
      panel.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
          plugin.focusOn(friend);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
      });
      friend_components.put(
          friend.friend,
          friends.add(panel));
    }
    prune();
    friends.revalidate();
  }

  private JSeparator getJSeparator(Color color) {
    JSeparator sep = new JSeparator();
    sep.setBackground(color);
    sep.setForeground(color);
    return sep;
  }
}
