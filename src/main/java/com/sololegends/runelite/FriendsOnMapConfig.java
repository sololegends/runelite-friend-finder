package com.sololegends.runelite;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("friend-finder")
public interface FriendsOnMapConfig extends Config {
	@ConfigItem(keyName = "friend_api", name = "Friends API", description = "Wen API to send and retrieve location data to/from")
	default String friendsAPI() {
		return "https://runelite.sololegends.com/friends";
	}
}
