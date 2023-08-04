package com.sololegends.runelite.skills;

import net.runelite.api.Skill;

public abstract class PlayerSkill {
	int current, max;
	final Skill skill;

	protected PlayerSkill(Skill skill, int current, int max) {
		this.skill = skill;
		this.current = current;
		this.max = max;
	}

	public int current() {
		return current;
	}

	public Skill type() {
		return skill;
	}

	public int max() {
		return max;
	}

}
