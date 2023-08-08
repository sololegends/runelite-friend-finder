package com.sololegends.runelite.panel;

import java.awt.Color;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.sololegends.runelite.FriendMapPoint;
import com.sololegends.runelite.helpers.WorldLocations.WorldSurface;
import com.sololegends.runelite.skills.*;

import net.runelite.client.ui.components.ProgressBar;

public class FriendPanel extends JPanel {

	// Time static
	private static final long EXPIRE_THRESHOLD = 90_000;

	// Color static
	private static final Color HEALTH_FG = new Color(0, 146, 54, 230);
	private static final Color HEALTH_BG = new Color(102, 15, 16, 230);
	private static final Color PRAYER_FG = new Color(0, 149, 151);
	private static final Color PRAYER_BG = Color.black;

	ProgressBar health = buildStatBar(new Health(0, 0));
	ProgressBar prayer = buildStatBar(new Prayer(0, 0));
	TitledBorder border = new TitledBorder("");
	final FriendsPanel parent;
	long updated = System.currentTimeMillis();

	public FriendPanel(FriendsPanel parent, FriendMapPoint friend, ImageIcon h_icon, ImageIcon p_icon) {
		border = new TitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				friend.friend);
		this.setBorder(border);
		this.parent = parent;
		updated = System.currentTimeMillis();

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		health = buildStatBar(friend.getHealth());
		prayer = buildStatBar(friend.getPrayer());

		JLabel health_icon = new JLabel(h_icon);
		JLabel prayer_icon = new JLabel(p_icon);

		GroupLayout.SequentialGroup h_group = layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(layout.createSequentialGroup()
								.addComponent(health_icon, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, 25)
								.addComponent(health, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
						.addGroup(layout.createSequentialGroup()
								.addComponent(prayer_icon, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, 25)
								.addComponent(prayer, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)));

		GroupLayout.SequentialGroup v_group = layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(health_icon)
						.addComponent(health))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(prayer_icon)
						.addComponent(prayer));

		layout.setHorizontalGroup(h_group);
		layout.setVerticalGroup(v_group);
	}

	public void update(FriendMapPoint friend, WorldSurface surface) {
		updated = System.currentTimeMillis();
		Health h_skill = friend.getHealth();
		Prayer p_skill = friend.getPrayer();

		health.setValue(h_skill.current());
		health.setMaximumValue(h_skill.max());
		health.setCenterLabel(h_skill.current() + " / " + h_skill.max());

		prayer.setValue(p_skill.current());
		prayer.setMaximumValue(p_skill.max());
		prayer.setCenterLabel(p_skill.current() + " / " + p_skill.max());

		if (surface != null) {
			border.setTitle(friend.friend + ": " + surface.name);
		}
	}

	public boolean expired() {
		return System.currentTimeMillis() - updated > EXPIRE_THRESHOLD;
	}

	private ProgressBar buildStatBar(PlayerSkill skill) {
		ProgressBar bar = new ProgressBar();
		bar.setValue(skill.current());
		bar.setMaximumValue(skill.max());
		bar.setCenterLabel(skill.current() + " / " + skill.max());

		if (skill.type() == net.runelite.api.Skill.HITPOINTS) {
			bar.setForeground(HEALTH_FG);
			bar.setBackground(HEALTH_BG);
		} else if (skill.type() == net.runelite.api.Skill.PRAYER) {
			bar.setForeground(PRAYER_FG);
			bar.setBackground(PRAYER_BG);
		}

		return bar;
	}
}
