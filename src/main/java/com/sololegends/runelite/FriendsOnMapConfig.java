package com.sololegends.runelite;

import java.awt.Color;

import net.runelite.client.config.*;

@ConfigGroup("Friend Finder")
public interface FriendsOnMapConfig extends Config {

	@ConfigSection(name = "Styling", description = "Stylize it!", position = 10)
	String styling_section = "styling";

	@ConfigSection(name = "Debug", description = "Debugging", position = 30, closedByDefault = true)
	String debug_section = "debug";

	public static enum UpdateIntervalEnum {
		SECONDS_1("1 Second", 1),
		SECONDS_2("2 Seconds", 2),
		SECONDS_5("5 Seconds", 5),
		SECONDS_10("10 Seconds", 10),
		SECONDS_20("20 Seconds", 20),
		SECONDS_30("30 Seconds", 30),
		SECONDS_60("60 Seconds", 60);

		private final String name;
		private final int interval;

		UpdateIntervalEnum(String name, int interval) {
			this.name = name;
			this.interval = interval;
		}

		public int interval() {
			return interval * 1000;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@ConfigItem(position = 0, section = "General", keyName = "dbl_click_ch_world", name = "Double Click to change world", description = "When true double clicking a friend's icon will switch to their world")
	default boolean dblClickWorldHop() {
		return false;
	}

	@ConfigItem(position = 1, section = "General", keyName = "friend_api", name = "Friends API", description = "What API to send and retrieve location data to/from")
	default String friendsAPI() {
		return "https://runelite.sololegends.com/friends";
	}

	@ConfigItem(position = 2, section = "General", keyName = "update_interval", name = "Update Interval", description = "Interval between sending/receiving your/friends locations")
	default UpdateIntervalEnum updateInterval() {
		return UpdateIntervalEnum.SECONDS_2;
	}

	// STYLING CONFIGURATION

	@ConfigItem(position = 11, section = styling_section, keyName = "style_dot_color", name = "Dot Color", description = "What color the dot is for your friends on the main map")
	default Color dotColor() {
		return Color.GREEN;
	}

	@ConfigItem(position = 12, section = styling_section, keyName = "style_other_world_color", name = "Other World Color", description = "Outline color for the dots when the friend is NOt on your world")
	default Color otherWorldColor() {
		return Color.GREEN.darker().darker().darker();
	}

	@ConfigItem(position = 13, section = styling_section, keyName = "style_link_dot_color", name = "Link Dot Color", description = "What color the dot is for your friends on the main map. Used when the icon links to a sub/dungeon area")
	default Color dotColorLink() {
		return Color.ORANGE;
	}

	@ConfigItem(position = 14, section = styling_section, keyName = "style_link_other_world_color", name = "Link Other World Color", description = "Outline color for the dots when the friend is NOt on your world. Used when the icon links to a sub/dungeon area")
	default Color otherWorldColorLink() {
		return Color.ORANGE.darker().darker().darker();
	}

	@ConfigItem(position = 15, section = styling_section, keyName = "style_dot_size", name = "Dot Size", description = "Pixel size for the dots on the main map")
	@Range(max = 60, min = 5)
	default int dotSize() {
		return 16;
	}

	@ConfigItem(position = 16, section = styling_section, keyName = "off_world_as_outline", name = "Off World as Outline", description = "When set, the off world status will be shown as an outline instead of full color change")
	default boolean offWorldAsOutline() {
		return true;
	}

	@ConfigItem(position = 17, section = styling_section, keyName = "style_outline_size", name = "Outline Size", description = "When off world indication is set to outline, this defines the outline width")
	@Range(max = 30, min = 2)
	default int outlineSize() {
		return 3;
	}

	// Debugging
	@ConfigItem(position = 31, section = debug_section, keyName = "show_self_location_card", name = "Show your location card", description = "Shows your tracked location card at the top left of the game window")
	default boolean yourLocation() {
		return false;
	}

	@ConfigItem(position = 32, section = debug_section, keyName = "map_areas", name = "Map Draw Areas", description = "Turns on drawing every sub area tracked on the map surfaces")
	default boolean mapDrawAreas() {
		return false;
	}

}
