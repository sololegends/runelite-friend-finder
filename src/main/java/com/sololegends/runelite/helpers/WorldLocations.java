package com.sololegends.runelite.helpers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.sololegends.runelite.data.WorldRegions;

import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public class WorldLocations {

  private static final Set<WorldSurface> WORLD_AREAS = ConcurrentHashMap.newKeySet();

  // y and y2 are inverted because RS using a flipped y axis, this is for easy
  // listing
  public static WorldArea fromBounds(int x, int y2, int x2, int y) {
    return new WorldArea(x, y, x2 - x, y2 - y, 0);
  }

  public static WorldSurface surface(String name, int eX, int eY, WorldArea... areas) {
    return new WorldSurface(name, new WorldPoint(eX, eY, 0), areas);
  }

  public static Set<WorldSurface> getSurfaces() {
    return new HashSet<WorldSurface>(WORLD_AREAS);
  }

  static {
    WORLD_AREAS.add(surface("RuneScape Surface", 00, 00, fromBounds(1052, 4132, 3940, 2396)));
    // Ancient Cavern
    WORLD_AREAS.add(surface("Ancient Cavern", 2511, 3508, fromBounds(1733, 5436, 1854, 5318)));
    // Ardougne Underground
    WorldSurface ardougne_ug = surface("Ardougne Underground", 2631, 3294, fromBounds(2513, 9854, 2748, 9599));
    // WORLD_AREAS.add(ardougne_ug);
    WORLD_AREAS.add(surface("Clock Tower Dungeon", 2568, 3229, fromBounds(2560, 9663, 2623, 9600))
        .setParent(ardougne_ug));
    WORLD_AREAS.add(surface("Ardougne Rat Pits", 2560, 3320, fromBounds(2640, 9675, 2673, 9617))
        .setParent(ardougne_ug));
    WORLD_AREAS.add(surface("Ardougne Sewers North", 2641, 9674, fromBounds(2628, 9727, 2684, 9675))
        .setParent(ardougne_ug));
    WORLD_AREAS.add(surface("Ardougne Sewers South", 2586, 3235,
        fromBounds(2536, 9727, 2623, 9664), fromBounds(2513, 9664, 2557, 9638))
        .setParent(ardougne_ug));
    WORLD_AREAS.add(surface("Chaos Druid Tower Dungeon", 2561, 3356, fromBounds(2560, 9758, 2593, 9729))
        .setParent(ardougne_ug));
    WORLD_AREAS.add(surface("Goblin Cave", 2262, 3393, fromBounds(2561, 9854, 2622, 9793))
        .setParent(ardougne_ug));
    WORLD_AREAS.add(surface("Legend's Guild Dungeon", 2723, 3375, fromBounds(2690, 9785, 2739, 9729))
        .setParent(ardougne_ug));
    WORLD_AREAS.add(surface("Wicthaven Dungeon", 2695, 3283, fromBounds(2692, 9719, 2748, 9665))
        .setParent(ardougne_ug));

    // Asgarnia Ice Cave
    WorldSurface asgarnia_ice_cave = surface("Asgarnia Ice Cave", 00, 00, fromBounds(2920, 9660, 3149, 9535));
    // WORLD_AREAS.add(asgarnia_ice_cave);
    WORLD_AREAS.add(surface("Melzar's Maze Basement", 2923, 3250, fromBounds(2920, 9660, 2942, 9638))
        .setParent(asgarnia_ice_cave));
    WORLD_AREAS.add(surface("Port Sarim Rat Pits", 3017, 3232, fromBounds(3016, 9658, 3063, 9623))
        .setParent(asgarnia_ice_cave));
    WORLD_AREAS.add(surface("Asgarnia Ice Dungeon", 3005, 3150, fromBounds(2984, 9601, 3145, 9538))
        .setParent(asgarnia_ice_cave));

    // Braindeath Island
    WORLD_AREAS.add(surface("Braindeath Island", 3680, 3537, fromBounds(2113, 5182, 2174, 5057)));

    // Dorgesh-Kaan
    WORLD_AREAS.add(surface("Dorgesh-Kaan", 3208, 3618, fromBounds(2689, 5502, 2878, 5252)));

    // Dwarven Mines
    WorldSurface dwarven_mines = surface("Dwarven Mines", 00, 00, fromBounds(2962, 9852, 3223, 9664));
    // WORLD_AREAS.add(dwarven_mines);
    WORLD_AREAS.add(surface("Dwarven Mine North", 3017, 3450, fromBounds(2962, 9852, 3060, 9794))
        .setParent(dwarven_mines));
    WORLD_AREAS.add(surface("Dwarven Mine South", 3058, 3376, fromBounds(3031, 9794, 3062, 9756))
        .setParent(dwarven_mines));
    WORLD_AREAS.add(surface("Mining Guild Basement", 3018, 3339, fromBounds(3002, 9756, 3056, 9699))
        .setParent(dwarven_mines));
    WORLD_AREAS.add(surface("Motherload Mine", 3058, 3376, fromBounds(3074, 9725, 3223, 9666))
        .setParent(dwarven_mines));

    // Feldip Hills Underground
    WorldSurface fh_under = surface("Feldip Hills Underground", 00, 00, fromBounds(1669, 9199, 2071, 8860));
    // WORLD_AREAS.add(fh_under);
    WORLD_AREAS.add(surface("Crumbling Tower Basement", 2130, 2994, fromBounds(1671, 9198, 1687, 9189))
        .setParent(fh_under));
    WORLD_AREAS.add(surface("Isle of Souls Dungeon", 3208, 2919, fromBounds(1793, 9149, 1853, 9093))
        .setParent(fh_under));
    WORLD_AREAS.add(surface("Red Chinchompa Hunting Ground", 2525, 2894, fromBounds(2015, 9092, 2038, 9071))
        .setParent(fh_under));
    WORLD_AREAS.add(surface("Myths' Guild Dungeon", 2456, 2847, fromBounds(1883, 9023, 1953, 8963))
        .setParent(fh_under));
    WORLD_AREAS.add(surface("Corsair Cove Dungeon", 2525, 2894,
        fromBounds(1953, 9085, 1987, 8970), fromBounds(1987, 9012, 2072, 8962))
        .setParent(fh_under));

    // Fossil Island Underground
    WorldSurface fi_under = surface("Fossil Island Underground", 00, 00, fromBounds(3585, 10302, 3918, 10113));
    // WORLD_AREAS.add(fi_under);
    WORLD_AREAS.add(surface("Wyvern Cave (Task)", 3677, 3854, fromBounds(3585, 10302, 3645, 10242))
        .setParent(fi_under));
    WORLD_AREAS.add(surface("Wyvern Cave", 3745, 3779, fromBounds(3585, 10237, 3647, 10241))
        .setParent(fi_under));
    WORLD_AREAS.add(surface("Underwater", 3765, 3898, fromBounds(3716, 10301, 3837, 10241))
        .setParent(fi_under));
    WORLD_AREAS.add(surface("Volcanic Mine", 3815, 3808,
        fromBounds(3779, 10229, 3836, 10114), fromBounds(3890, 10239, 3917, 10190))
        .setParent(fi_under));

    // Fremennik Slayer Cave
    WORLD_AREAS.add(surface("Fremennik Slayer Cave", 2796, 3615, fromBounds(2691, 10044, 2811, 9920)));
    // God Wars Dungeon
    WORLD_AREAS.add(surface("God Wars Dungeon", 2917, 3746, fromBounds(2820, 5373, 2962, 5157)));

    // Karamja Underground
    WorldSurface karamja_ug = surface("Karamja Underground", 00, 00, fromBounds(2560, 9662, 2940, 9375));
    // WORLD_AREAS.add(karamja_ug);
    WORLD_AREAS.add(surface("Crandor Dungeon", 2833, 3256, fromBounds(2827, 9661, 2866, 9546))
        .setParent(karamja_ug));
    WORLD_AREAS.add(surface("Jogre Dungeon", 2824, 3118, fromBounds(2822, 9532, 2939, 9414))
        .setParent(karamja_ug));
    WORLD_AREAS.add(surface("Shilo Village Mine", 2824, 2942, fromBounds(2826, 9399, 2858, 9377))
        .setParent(karamja_ug));
    WORLD_AREAS.add(surface("Brimhaven Agility Arena", 2808, 3194, fromBounds(2757, 9594, 2809, 9542))
        .setParent(karamja_ug));
    WORLD_AREAS.add(surface("Brimhaven Dungeon North", 2743, 3154,
        fromBounds(2561, 9598, 2618, 9473), fromBounds(2629, 9598, 2734, 9498))
        .setParent(karamja_ug));
    WORLD_AREAS.add(surface("Brimhaven Dungeon South", 2759, 3062,
        fromBounds(2627, 9498, 2749, 9412), fromBounds(2730, 9510, 2746, 9496))
        .setParent(karamja_ug));

    // Kebos Underground
    WorldSurface kebos_ug = surface("Kebos Underground", 00, 00, fromBounds(1115, 10287, 1380, 9925));
    // WORLD_AREAS.add(kebos_ug);
    WORLD_AREAS.add(surface("Karuulm Slayer Dungeon", 1308, 3807, fromBounds(1117, 10285, 1379, 10129))
        .setParent(kebos_ug));
    WORLD_AREAS.add(surface("Hespori Patch", 1232, 3729, fromBounds(1175, 10079, 1191, 10063))
        .setParent(kebos_ug));
    WORLD_AREAS.add(surface("Lizardman Temple", 1311, 3685, fromBounds(1281, 10063, 1343, 9999))
        .setParent(kebos_ug));
    WORLD_AREAS.add(surface("Lizardman Caves", 1306, 3574, fromBounds(1284, 9958, 1335, 9927))
        .setParent(kebos_ug));

    // Keldagrim
    WORLD_AREAS.add(surface("Keldagrim", 2730, 3713, fromBounds(2816, 10239, 2943, 10079)));

    // Kharidian Desert Underground
    WorldSurface kharidian_ug = surface("Kharidian Desert Underground", 00, 00, fromBounds(1244, 10212, 1956, 9692));
    // WORLD_AREAS.add(kharidian_ug);
    WORLD_AREAS.add(surface("Kalphite Cave", 3319, 3122, fromBounds(3330, 9542, 3404, 9474))
        .setParent(kharidian_ug));
    WORLD_AREAS.add(surface("Kalphite Lair", 3226, 3108, fromBounds(3171, 9530, 3230, 9408))
        .setParent(kharidian_ug));
    WORLD_AREAS.add(surface("Giant's Foundry", 3360, 3150, fromBounds(3432, 9606, 3477, 9578))
        .setParent(kharidian_ug));

    // Kourend Underground
    WorldSurface kourend_ug = surface("Kourend Underground", 00, 00, fromBounds(1347, 10173, 1877, 9793));
    // WORLD_AREAS.add(kourend_ug);
    WORLD_AREAS.add(surface("Kourend Underground", 1470, 3653, fromBounds(1600, 10111, 1729, 9979))
        .setParent(kourend_ug));
    WORLD_AREAS.add(surface("Chasm of Fire", 1433, 3670, fromBounds(1346, 10107, 1404, 9859))
        .setParent(kourend_ug));
    WORLD_AREAS.add(surface("Shayzien Crypts", 1482, 3549, fromBounds(1446, 10107, 1497, 9865))
        .setParent(kourend_ug));
    WORLD_AREAS.add(surface("Giant's Den", 1420, 3588, fromBounds(1413, 9850, 1467, 9795))
        .setParent(kourend_ug));
    WORLD_AREAS.add(surface("Woodcutting Guild Dungeon", 1603, 3508, fromBounds(1544, 9914, 1598, 9862))
        .setParent(kourend_ug));
    WORLD_AREAS.add(surface("Crabclaw Caves", 1643, 3449, fromBounds(1644, 9849, 1723, 9792))
        .setParent(kourend_ug));
    WORLD_AREAS.add(surface("Forthos dungeon", 1701, 3574, fromBounds(1794, 9979, 1854, 9882))
        .setParent(kourend_ug));
    WORLD_AREAS.add(surface("The Warrens", 1812, 3745, fromBounds(1793, 10174, 1876, 10114))
        .setParent(kourend_ug));

    // Lair of Tarn Razorlor
    WORLD_AREAS.add(surface("Lair of Tarn Razorlor", 00, 00, fromBounds(3138, 4637, 3390, 4545)));

    // LMS Desert Island
    WORLD_AREAS.add(surface("LMS Desert Island", 3138, 3635, fromBounds(3392, 5887, 3519, 5760)));

    // LMS Wild Varrock
    WORLD_AREAS.add(surface("LMS Wild Varrock", 3138, 3635, fromBounds(3456, 6206, 3646, 6016)));

    WorldSurface misc_ug = surface("Miscellania Underground", 00, 00, fromBounds(2012, 10468, 2916, 10076));
    // WORLD_AREAS.add(misc_ug);
    WORLD_AREAS.add(surface("Miscellania / Etcetera Dungeon", 2619, 3865, fromBounds(2500, 10301, 2621, 10243))
        .setParent(misc_ug));
    WORLD_AREAS.add(surface("Ice Troll Cave", 2401, 3889, fromBounds(2375, 10302, 2426, 10241))
        .setParent(misc_ug));
    WORLD_AREAS.add(surface("Jormungand's Prison", 2464, 4012, fromBounds(2398, 10467, 2492, 10371))
        .setParent(misc_ug));
    WORLD_AREAS.add(surface("Salt Mine", 2866, 3941, fromBounds(2829, 10354, 2858, 10326))
        .setParent(misc_ug));

    // Misthalin Underground
    WorldSurface mist_ug = surface("Misthalin Underground", 00, 00, fromBounds(2972, 10084, 3364, 9372));
    // WORLD_AREAS.add(mist_ug);
    WORLD_AREAS.add(surface("Edgeville Dungeon", 3115, 3452, fromBounds(3073, 9999, 3152, 9793))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Varrock Sewers", 3241, 3428, fromBounds(3153, 9918, 3287, 9858))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Draynor Manor Basement", 3114, 3357, fromBounds(3075, 9778, 3118, 9744))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Champions' Challenge", 3189, 3355, fromBounds(3150, 9772, 3188, 9743))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("VTAM Corporation", 3244, 3384,
        fromBounds(3230, 9806, 3254, 9761), fromBounds(3187, 9834, 3196, 9818))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Draynor Sewers", 3083, 3272, fromBounds(3081, 9697, 3126, 9643))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Sourhog Cave", 3150, 3347, fromBounds(3153, 9719, 3182, 9670))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Watermill Cellar", 2866, 3941, fromBounds(3206, 9705, 3246, 9662))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("H.A.M. Cult", 2866, 3941, fromBounds(3140, 9658, 3188, 9606))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Lumbridge Castle Cellar", 3209, 3218, fromBounds(3208, 9625, 3219, 9615))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Goblin Mine", 3209, 3218, fromBounds(3309, 9654, 3326, 9600))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Goblin Maze", 3209, 3218, fromBounds(3221, 9658, 3308, 9603))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Lumbridge Swamp Caves", 3168, 3172, fromBounds(3144, 9601, 3261, 9487))
        .setParent(mist_ug));
    WORLD_AREAS.add(surface("Wizard's Tower Basement", 3103, 3162, fromBounds(3096, 9577, 3121, 9555))
        .setParent(mist_ug));

    // Mole Hole
    WORLD_AREAS.add(surface("Mole Hole", 2985, 3387, fromBounds(1732, 5244, 1787, 5132)));

    // TODO: Morytania Underground
    WORLD_AREAS.add(surface("Morytania Underground", 00, 00, fromBounds(3386, 9975, 3903, 9581)));

    // Mos Le'Harmless Cave
    WORLD_AREAS.add(surface("Mos Le'Harmless Cave", 3747, 2973, fromBounds(3716, 9469, 3837, 9348)));

    // Ourania Altar
    WORLD_AREAS.add(surface("Ourania Altar", 2450, 3231, fromBounds(3009, 5630, 3070, 5569)));

    // Stronghold of Security
    WORLD_AREAS.add(surface("Stronghold of Security", 3080, 3421,
        fromBounds(1856, 5247, 1915, 5184),
        fromBounds(1984, 5247, 2042, 5184),
        fromBounds(2113, 5247, 2176, 5184),
        fromBounds(2304, 5247, 2367, 5184)));

    // Stronghold Underground
    WorldSurface strong_ug = surface("Stronghold Underground", 00, 00, fromBounds(2140, 10084, 2596, 9628));
    // WORLD_AREAS.add(strong_ug);
    WORLD_AREAS.add(surface("Kraken Cave", 2277, 3611, fromBounds(2242, 10044, 2301, 9987))
        .setParent(strong_ug));
    WORLD_AREAS.add(surface("Grand Tree Tunnels", 2462, 3496, fromBounds(2433, 9918, 2494, 9858))
        .setParent(strong_ug));
    WORLD_AREAS.add(surface("Brimstail Cave", 2401, 3419,
        fromBounds(2379, 9830, 2396, 9805), fromBounds(2397, 9832, 2414, 9811))
        .setParent(strong_ug));
    WORLD_AREAS.add(surface("Stronghold Slayer Dungeon", 2427, 3424,
        fromBounds(2415, 9838, 2495, 9768), fromBounds(2392, 9806, 2414, 9768),
        fromBounds(2388, 9787, 2392, 9777))
        .setParent(strong_ug));

    // TODO Taverley Underground
    WORLD_AREAS.add(surface("Taverley Underground", 00, 00, fromBounds(2656, 10006, 2994, 9602)));

    // The Abyss
    WORLD_AREAS.add(surface("The Abyss", 3082, 3487, fromBounds(2950, 4924, 3131, 4737)));

    // Tolna's Rift
    WORLD_AREAS.add(surface("Tolna's Rift", 3308, 3449, fromBounds(3266, 9855, 3326, 9793)));

    // Troll Stronghold
    WORLD_AREAS.add(surface("Troll Stronghold", 2738, 3543, fromBounds(2822, 10174, 2990, 10049)));

    // Tutorial Island
    WORLD_AREAS.add(surface("Tutorial Island", 3127, 3037, fromBounds(1640, 6141, 1760, 6057)));

    // ! Waterbirth Dungeon
    // WORLD_AREAS.add(surface("Waterbirth Dungeon", 00, 00,
    // fromBounds(2435, 10174, 2733, 9826), fromBounds(2613, 9790, 2718, 9646)));

    // TODO Wilderness Dungeons
    WORLD_AREAS.add(surface("Wilderness Dungeons", 00, 00, fromBounds(2935, 10369, 3451, 10047)));

    // Yanille Underground
    WorldSurface yanille_ug = surface("Yanille Underground", 00, 00, fromBounds(2204, 9572, 2724, 9308));
    // WORLD_AREAS.add(yanille_ug);
    WORLD_AREAS.add(surface("Smoke Devils Dungeon", 2412, 3061, fromBounds(2347, 9468, 2426, 9415))
        .setParent(yanille_ug));
    WORLD_AREAS.add(surface("Wizard's Guild Basement", 2593, 3085, fromBounds(2582, 9493, 2594, 9484))
        .setParent(yanille_ug));
    WORLD_AREAS.add(surface("Yanille Agility Dungeon", 2603, 3078,
        fromBounds(2595, 9525, 2639, 9474), fromBounds(2561, 9533, 2595, 9496))
        .setParent(yanille_ug));

    // Zanaris
    WORLD_AREAS.add(surface("Zanaris", 3203, 3169, fromBounds(2370, 4478, 2493, 4355)));

    // Temporos
    WORLD_AREAS.add(surface("Temporos", 3136, 2841, fromBounds(12649, 26554478, 12699, 2605)));
  }

  public static WorldSurface getWorldSurface(WorldPoint point) {
    WorldSurface s = getWorldSurface(point.getRegionID());
    if (s != null && !s.name.equals("Unknown")) {
      return s;
    }
    for (WorldSurface surface : WORLD_AREAS) {
      if (surface.contains(point)) {
        return surface;
      }
    }
    return null;
  }

  public static WorldSurface getWorldSurface(int region) {
    if (region != -1) {
      WorldSurface s = WorldRegions.fromRegion(region);
      if (s != null) {
        return s;
      }
    }
    return null;
  }

  public static final class WorldSurface {
    public final String name;
    public final WorldArea[] areas;
    public final WorldPoint entry;
    public WorldSurface parent;

    public WorldSurface(String name, WorldPoint entry, WorldArea... areas) {
      this.name = name;
      this.areas = areas;
      this.entry = entry;
    }

    public WorldSurface setParent(WorldSurface parent) {
      this.parent = parent;
      return this;
    }

    public boolean contains(WorldPoint point) {
      for (WorldArea area : areas) {
        if (area.getX() <= point.getX() && point.getX() <= area.getX() + area.getWidth()
            && area.getY() <= point.getY() && point.getY() <= area.getY() + area.getHeight()) {
          return true;
        }
      }
      return false;
    }
  }

}
