package com.sololegends.runelite;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FriendsOnMapPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(FriendsOnMapPlugin.class);
		RuneLite.main(args);
	}
}