package com.sololegends.runelite.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.sololegends.runelite.*;
import com.sololegends.runelite.helpers.RemoteDataManager;
import com.sololegends.runelite.helpers.RemoteDataManager.UpdateFlow;

import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.*;
import net.runelite.client.ui.components.DragAndDropReorderPane;
import net.runelite.client.ui.components.FlatTextField;

public class FriendsPanel extends PluginPanel {

  private final FriendsOnMapPlugin plugin;

  private final JComponent friends = new DragAndDropReorderPane();
  private final Map<String, Component> friend_components = new ConcurrentHashMap<>();

  private final ImageIcon health_icon;
  private final ImageIcon prayer_icon;

  @Inject
  private RemoteDataManager remote;

  @Inject
  FriendsPanel(final FriendsOnMapPlugin plugin, final FriendsOnMapConfig config, final SkillIconManager icon_manager) {
    this.plugin = plugin;
    health_icon = new ImageIcon(icon_manager.getSkillImage(Skill.HITPOINTS, true));
    prayer_icon = new ImageIcon(icon_manager.getSkillImage(Skill.PRAYER, true));

    setBorder(new EmptyBorder(10, 10, 10, 10));
    setBackground(ColorScheme.DARK_GRAY_COLOR);
    setLayout(new BorderLayout());

    // Missing location report panel
    final JPanel report_panel = new JPanel(new BorderLayout());
    report_panel.setLayout(new BorderLayout());
    report_panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // ===== TITLE =====
    final JLabel report_missing = new JLabel("Report Missing Location");
    report_missing.setFont(FontManager.getRunescapeBoldFont());
    report_missing.setHorizontalAlignment(JLabel.CENTER);

    final JPanel title_panel = new JPanel(new BorderLayout());
    title_panel.add(report_missing, BorderLayout.NORTH);

    // ===== TEXT BLURB =====

    final JPanel report_info_panel = new JPanel(new BorderLayout());
    report_info_panel.setLayout(new BoxLayout(report_info_panel, BoxLayout.Y_AXIS));
    report_info_panel.add(title_panel, BorderLayout.NORTH);
    report_info_panel.add(Box.createRigidArea(new Dimension(0, 3)));
    report_info_panel.add(getJSeparator(ColorScheme.LIGHT_GRAY_COLOR));
    report_info_panel.add(Box.createRigidArea(new Dimension(0, 3)));
    report_info_panel.add(getTextArea(
        "Enter where you are in the field below, then click submit location to submit your report for review and approval"),
        BorderLayout.CENTER);
    report_info_panel.add(Box.createRigidArea(new Dimension(0, 3)));
    report_info_panel.add(getJSeparator(ColorScheme.LIGHT_GRAY_COLOR));

    // REPORT ACTION PANEL

    final JPanel report_action_panel = new JPanel(new BorderLayout());

    JTextField location_input = getTextField(report_action_panel, "Your Location");
    final String SUBMIT_TEXT = "Report Missing Location";
    final JButton submit = new JButton(SUBMIT_TEXT);
    submit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String location = location_input.getText();
        // Build Payload
        JsonObject payload = plugin.getPlayerLocation();
        payload.addProperty("l", location);
        // Build and send the request
        remote.sendReport(payload, new UpdateFlow() {
          @Override
          public void success(String message) {
            submit.setText(message);
            location_input.setText("");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
              @Override
              public void run() {
                submit.setText(SUBMIT_TEXT);
              }
            }, 10_000l);

          }

          @Override
          public void error(String message) {
            submit.setText(message);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
              @Override
              public void run() {
                submit.setText(SUBMIT_TEXT);
              }
            }, 10_000l);

          }

        });
        // LinkBrowser.browse(config.reportLink());
      }
    });

    report_action_panel.add(submit, BorderLayout.SOUTH);

    // FINAL PANEL

    report_panel.setLayout(new BoxLayout(report_panel, BoxLayout.Y_AXIS));
    report_panel.add(title_panel);
    report_panel.add(Box.createRigidArea(new Dimension(0, 5)));
    report_panel.add(report_info_panel);
    report_panel.add(Box.createRigidArea(new Dimension(0, 10)));
    report_panel.add(report_action_panel);

    // END

    // Info Panel (just header atm)
    final JPanel info_panel = new JPanel();
    info_panel.setBorder(new EmptyBorder(0, 0, 4, 0));
    info_panel.add(new JLabel("Friends Info"));

    add(info_panel, BorderLayout.PAGE_START);
    add(friends, BorderLayout.CENTER);
    add(report_panel, BorderLayout.PAGE_END);

    revalidate();
    repaint();
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

  public JTextArea getTextArea(String text) {
    JTextArea text_area = new JTextArea(2, 20);
    text_area.setText(text);
    text_area.setWrapStyleWord(true);
    text_area.setLineWrap(true);
    text_area.setOpaque(false);
    text_area.setEditable(false);
    text_area.setFocusable(false);
    text_area.setBackground(ColorScheme.DARK_GRAY_COLOR);
    Font text_area_font = FontManager.getRunescapeSmallFont();
    text_area_font = text_area_font.deriveFont(text_area_font.getStyle(),
        (float) text_area_font.getSize() - (float) 0.1);
    text_area.setFont(text_area_font);

    text_area.setBorder(new EmptyBorder(0, 0, 0, 0));
    return text_area;
  }

  private JTextField getTextField(JPanel panel, String label) {
    final JPanel container = new JPanel();
    container.setLayout(new BorderLayout());

    final JLabel j_label = new JLabel(label);
    final FlatTextField input = new FlatTextField();

    input.setBackground(ColorScheme.DARKER_GRAY_COLOR);
    input.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
    input.setBorder(new EmptyBorder(5, 7, 5, 7));

    j_label.setFont(FontManager.getRunescapeSmallFont());
    j_label.setBorder(new EmptyBorder(0, 0, 4, 0));
    j_label.setForeground(Color.WHITE);

    container.add(j_label, BorderLayout.NORTH);
    container.add(input, BorderLayout.CENTER);
    container.add(Box.createRigidArea(new Dimension(0, 5)), BorderLayout.SOUTH);

    panel.add(container, BorderLayout.CENTER);

    return input.getTextField();
  }
}
