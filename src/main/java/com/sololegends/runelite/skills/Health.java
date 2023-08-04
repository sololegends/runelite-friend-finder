package com.sololegends.runelite.skills;

public class Health extends PlayerSkill {

	public Health(int current, int max) {
		super(net.runelite.api.Skill.HITPOINTS, current, max);
	}
}
