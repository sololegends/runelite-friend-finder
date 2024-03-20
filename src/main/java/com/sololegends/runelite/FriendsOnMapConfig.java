package com.sololegends.runelite;

import java.awt.Color;

import net.runelite.client.config.*;

@ConfigGroup("Friend Finder")
public interface FriendsOnMapConfig extends Config {

  @ConfigSection(name = "Styling", description = "Stylize it!", position = 10)
  String styling_section = "styling";

  @ConfigSection(name = "Custom API", description = "Set a custom private API", position = 20)
  String custom_section = "custom";

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

  @ConfigItem(position = 3, section = "General", keyName = "update_interval", name = "Update Interval", description = "Interval between sending/receiving your/friends locations")
  default UpdateIntervalEnum updateInterval() {
    return UpdateIntervalEnum.SECONDS_2;
  }

  @ConfigItem(position = 4, section = "General", keyName = "enable_sidebar", name = "Enable Sidebar", description = "Whether to enable the Friends On Map sidebar")
  default boolean showSidebarIcon() {
    return true;
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

  @ConfigItem(position = 18, section = styling_section, keyName = "always_show_name", name = "Name on World Map", description = "When set, friend's name and world will always be shows on the world map. On hover otherwise")
  default boolean alwaysShowName() {
    return false;
  }

  // CUSTOM API STUFF
  @ConfigItem(position = 21, section = custom_section, keyName = "friend_api", name = "Friends API", description = "What API to send and retrieve location data to/from")
  default String friendsAPI() {
    return "https://runelite.sololegends.com/friends";
  }

  @ConfigItem(position = 22, section = custom_section, keyName = "friend_api_key", name = "Friends API Key", description = "API Key to send as a header to the API on each request, only for private servers")
  default String friendsAPIKey() {
    return "";
  }

  @ConfigItem(position = 23, section = custom_section, keyName = "friend_report_api", name = "Reports Link", description = "Link to open when reporting a missing location")
  default String reportLink() {
    return "https://runelite.sololegends.com/location/report";
  }

  @ConfigItem(position = 24, section = custom_section, keyName = "friend_server_loc_api", name = "Server Locations API", description = "URL to pull JSON set of locations from a server")
  default String locationsLink() {
    return "https://runelite.sololegends.com/locations";
  }

  // DEBUGGING
  @ConfigItem(position = 31, section = debug_section, keyName = "show_self_location_card", name = "Show your location card", description = "Shows your tracked location card at the top left of the game window")
  default boolean yourLocation() {
    return false;
  }

  @ConfigItem(position = 32, section = debug_section, keyName = "map_areas", name = "Map Draw Areas", description = "Turns on drawing every sub area tracked on the map surfaces")
  default boolean mapDrawAreas() {
    return false;
  }

  @ConfigItem(position = 33, section = debug_section, keyName = "fake_friends", name = "Add Fake Friends", description = "Adds some fake friends for debugging when you have no friends")
  default boolean fakeFriends() {
    return false;
  }

  @ConfigItem(keyName = "fake_friends", name = "", description = "")
  void fakeFriends(boolean fake_friends);

}
