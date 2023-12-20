package com.sololegends.runelite.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sololegends.runelite.helpers.WorldLocations.WorldSurface;

import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

public class WorldRegions {

  private static final Map<Integer, WorldSurface> REGIONS = new ConcurrentHashMap<>();

  // y and y2 are inverted because RS using a flipped y axis, this is for easy
  // listing
  public static WorldArea fromBounds(int x, int y2, int x2, int y) {
    return new WorldArea(x, y, x2 - x, y2 - y, 0);
  }

  public static WorldSurface surface(String name, int eX, int eY, WorldArea... areas) {
    return new WorldSurface(name, new WorldPoint(eX, eY, 0), areas);
  }

  private static void addRegion(WorldSurface surface, int... region_ids) {
    for (int i : region_ids) {
      if (REGIONS.containsKey(i)) {
        System.out.println("FFP => WARNING: Duplicate Region ID registered [" + surface.name + ":" + i + "]");
      }
      REGIONS.put(i, surface);
    }
  }

  public static WorldSurface fromRegion(int region) {
    return REGIONS.get(region);
  }

  static {
    // Bosses
    addRegion(surface("Abyssal Sire", 0, 0, fromBounds(0, 0, 0, 0)), 11851, 11850, 12363, 12362);
    addRegion(surface("Cerberus", 0, 0, fromBounds(0, 0, 0, 0)), 4883, 5140, 5395);
    addRegion(surface("Commander Zilyana", 0, 0, fromBounds(0, 0, 0, 0)), 11602);
    addRegion(surface("Corporeal Beast", 0, 0, fromBounds(0, 0, 0, 0)), 11842, 11844);
    addRegion(surface("Dagannoth Kings", 0, 0, fromBounds(0, 0, 0, 0)), 11588, 11589);
    addRegion(surface("General Graardor", 0, 0, fromBounds(0, 0, 0, 0)), 11347);
    addRegion(surface("Giant Mole", 0, 0, fromBounds(0, 0, 0, 0)), 6993, 6992);
    addRegion(surface("Grotesque Guardians", 0, 0, fromBounds(0, 0, 0, 0)), 6727);
    addRegion(surface("Hespori", 0, 0, fromBounds(0, 0, 0, 0)), 5021);
    addRegion(surface("Alchemical Hydra", 0, 0, fromBounds(0, 0, 0, 0)), 5536);
    addRegion(surface("Kalphite Queen", 0, 0, fromBounds(0, 0, 0, 0)), 13972);
    addRegion(surface("Kraken", 0, 0, fromBounds(0, 0, 0, 0)), 9116);
    addRegion(surface("Kree'arra", 0, 0, fromBounds(0, 0, 0, 0)), 11346);
    addRegion(surface("K'ril Tsutsaroth", 0, 0, fromBounds(0, 0, 0, 0)), 11603);
    addRegion(surface("Nex", 0, 0, fromBounds(0, 0, 0, 0)), 11601);
    addRegion(surface("Nightmare of Ashihama", 0, 0, fromBounds(0, 0, 0, 0)), 15515);
    addRegion(surface("Sarachnis", 0, 0, fromBounds(0, 0, 0, 0)), 7322);
    addRegion(surface("Skotizo", 0, 0, fromBounds(0, 0, 0, 0)), 6810);
    addRegion(surface("Thermonuclear smoke devil", 0, 0, fromBounds(0, 0, 0, 0)), 9363, 9619);
    addRegion(surface("Tempoross", 0, 0, fromBounds(0, 0, 0, 0)), 12078);
    addRegion(surface("Vorkath", 0, 0, fromBounds(0, 0, 0, 0)), 9023);
    addRegion(surface("Wintertodt", 0, 0, fromBounds(0, 0, 0, 0)), 6462);
    addRegion(surface("Zalcano", 0, 0, fromBounds(0, 0, 0, 0)), 12126);
    addRegion(surface("Zulrah", 0, 0, fromBounds(0, 0, 0, 0)), 9007);
    addRegion(surface("Skotizo", 0, 0, fromBounds(0, 0, 0, 0)), 9048);

    // Cities
    // !TBD

    // Dungeons (113)
    addRegion(surface("Abandoned Mine", 0, 0, fromBounds(0, 0, 0, 0)), 13618, 13718, 11079, 11078, 11077, 10823, 10822,
        10821);
    addRegion(surface("Ah Za Rhoon", 0, 0, fromBounds(0, 0, 0, 0)), 11666);
    addRegion(surface("Ancient Cavern", 0, 0, fromBounds(0, 0, 0, 0)), 6483, 6995);
    addRegion(surface("Ape Atoll Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 11150, 10894);
    addRegion(surface("Ape Atoll Banana Plantation", 0, 0, fromBounds(0, 0, 0, 0)), 10895);
    addRegion(surface("West Ardougne Basement", 0, 0, fromBounds(0, 0, 0, 0)), 10135);
    addRegion(surface("Ardougne Sewers", 0, 0, fromBounds(0, 0, 0, 0)), 10134, 10136, 10391, 10647);
    addRegion(surface("Asgarnian Ice Caves", 0, 0, fromBounds(0, 0, 0, 0)), 11925, 12181);
    addRegion(surface("Tomb of Bervirius", 0, 0, fromBounds(0, 0, 0, 0)), 11154);
    addRegion(surface("Brimhaven Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10901, 10900, 10899, 10645, 10644, 10643);
    addRegion(surface("Brine Rat Cavern", 0, 0, fromBounds(0, 0, 0, 0)), 10910);
    addRegion(surface("Catacombs of Kourend", 0, 0, fromBounds(0, 0, 0, 0)), 6557, 6556, 6813, 6812);
    addRegion(surface("Champions' Challenge", 0, 0, fromBounds(0, 0, 0, 0)), 12696);
    addRegion(surface("Chaos Druid Tower", 0, 0, fromBounds(0, 0, 0, 0)), 10392);
    addRegion(surface("Chasm of Fire", 0, 0, fromBounds(0, 0, 0, 0)), 5789);
    addRegion(surface("Chasm of Tears", 0, 0, fromBounds(0, 0, 0, 0)), 12948);
    addRegion(surface("Chinchompa Hunting Ground", 0, 0, fromBounds(0, 0, 0, 0)), 10129);
    addRegion(surface("Clock Tower Basement", 0, 0, fromBounds(0, 0, 0, 0)), 10390);
    addRegion(surface("Corsair Cove Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 8076, 8332);
    addRegion(surface("Crabclaw Caves", 0, 0, fromBounds(0, 0, 0, 0)), 6553, 6809);
    addRegion(surface("Crandor Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 11414);
    addRegion(surface("Crash Site Cavern", 0, 0, fromBounds(0, 0, 0, 0)), 8280, 8536);
    addRegion(surface("Crumbling Tower", 0, 0, fromBounds(0, 0, 0, 0)), 7827);
    addRegion(surface("Daeyalt Essence Mine", 0, 0, fromBounds(0, 0, 0, 0)), 14744);
    addRegion(surface("Digsite Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 13464, 13465);
    addRegion(surface("Dorgesh-Kaan South Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10833);
    addRegion(surface("Dorgeshuun Mines", 0, 0, fromBounds(0, 0, 0, 0)), 12950, 13206);
    addRegion(surface("Draynor Sewers", 0, 0, fromBounds(0, 0, 0, 0)), 12439, 12438);
    addRegion(surface("Dwarven Mines", 0, 0, fromBounds(0, 0, 0, 0)), 12185, 12184, 12183);
    addRegion(surface("Eagles' Peak Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 8013);
    addRegion(surface("Ectofuntus", 0, 0, fromBounds(0, 0, 0, 0)), 14746);
    addRegion(surface("Edgeville Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 12441, 12442, 12443, 12698);
    addRegion(surface("Elemental Workshop", 0, 0, fromBounds(0, 0, 0, 0)), 10906, 7760);
    addRegion(surface("Elven rabbit cave", 0, 0, fromBounds(0, 0, 0, 0)), 13252);
    addRegion(surface("Enakhra's Temple", 0, 0, fromBounds(0, 0, 0, 0)), 12423);
    addRegion(surface("Evil Chicken's Lair", 0, 0, fromBounds(0, 0, 0, 0)), 9796);
    addRegion(surface("Experiment Cave", 0, 0, fromBounds(0, 0, 0, 0)), 14235, 13979);
    addRegion(surface("Ferox Enclave Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 12700);
    addRegion(surface("Forthos Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 7323);
    addRegion(surface("Fremennik Slayer Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10907, 10908, 11164);
    addRegion(surface("Glarial's Tomb", 0, 0, fromBounds(0, 0, 0, 0)), 10137);
    addRegion(surface("Goblin Cave", 0, 0, fromBounds(0, 0, 0, 0)), 10393);
    addRegion(surface("Grand Tree Tunnels", 0, 0, fromBounds(0, 0, 0, 0)), 9882);
    addRegion(surface("H.A.M. Hideout", 0, 0, fromBounds(0, 0, 0, 0)), 12694);
    addRegion(surface("H.A.M. Store room", 0, 0, fromBounds(0, 0, 0, 0)), 10321);
    addRegion(surface("Heroes' Guild Mine", 0, 0, fromBounds(0, 0, 0, 0)), 11674);
    addRegion(surface("Iorwerth Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 12737, 12738, 12993, 12994);
    addRegion(surface("Isle of Souls Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 8593);
    addRegion(surface("Jatizso Mines", 0, 0, fromBounds(0, 0, 0, 0)), 9631);
    addRegion(surface("Jiggig Burial Tomb", 0, 0, fromBounds(0, 0, 0, 0)), 9875, 9874);
    addRegion(surface("Jogre Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 11412);
    addRegion(surface("Karamja Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 11413);
    addRegion(surface("Karuulm Slayer Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 5280, 5279, 5023, 5535, 5022, 4766, 4510,
        4511, 4767, 4768, 4512);
    addRegion(surface("KGP Headquarters", 0, 0, fromBounds(0, 0, 0, 0)), 10658);
    addRegion(surface("Kruk's Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 9358, 9359, 9360, 9615, 9616, 9871, 10125, 10126,
        10127, 10128, 10381, 10382, 10383, 10384, 10637, 10638, 10639, 10640);
    addRegion(surface("Legends' Guild Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10904);
    addRegion(surface("Lighthouse", 0, 0, fromBounds(0, 0, 0, 0)), 10140);
    addRegion(surface("Lizardman Caves", 0, 0, fromBounds(0, 0, 0, 0)), 5275);
    addRegion(surface("Lizardman Temple", 0, 0, fromBounds(0, 0, 0, 0)), 5277);
    addRegion(surface("Lumbridge Swamp Caves", 0, 0, fromBounds(0, 0, 0, 0)), 12693, 12949);
    addRegion(surface("Lunar Isle Mine", 0, 0, fromBounds(0, 0, 0, 0)), 9377);
    addRegion(surface("Maniacal Monkey Hunter Area", 0, 0, fromBounds(0, 0, 0, 0)), 11662);
    addRegion(surface("Meiyerditch Mine", 0, 0, fromBounds(0, 0, 0, 0)), 9544);
    addRegion(surface("Miscellania Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10144, 10400);
    addRegion(surface("Mogre Camp", 0, 0, fromBounds(0, 0, 0, 0)), 11924);
    addRegion(surface("Mos Le'Harmless Caves", 0, 0, fromBounds(0, 0, 0, 0)), 14994, 14995, 15251);
    addRegion(surface("Motherlode Mine", 0, 0, fromBounds(0, 0, 0, 0)), 14679, 14680, 14681, 14935, 14936, 14937, 15191,
        15192, 15193);
    addRegion(surface("Mourner Tunnels", 0, 0, fromBounds(0, 0, 0, 0)), 7752, 8008);
    addRegion(surface("Mouse Hole", 0, 0, fromBounds(0, 0, 0, 0)), 9046);
    addRegion(surface("Myreditch Laboratories", 0, 0, fromBounds(0, 0, 0, 0)), 14232, 14233, 14487, 14488);
    addRegion(surface("Myreque Hideout", 0, 0, fromBounds(0, 0, 0, 0)), 13721, 13974, 13977, 13978);
    addRegion(surface("Myths' Guild Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 7564, 7820, 7821);
    addRegion(surface("Observatory Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 9362);
    addRegion(surface("Ogre Enclave", 0, 0, fromBounds(0, 0, 0, 0)), 10387);
    addRegion(surface("Ourania Cave", 0, 0, fromBounds(0, 0, 0, 0)), 12119);
    addRegion(surface("Quidamortem Cave", 0, 0, fromBounds(0, 0, 0, 0)), 4763);
    addRegion(surface("Rashiliyta's Tomb", 0, 0, fromBounds(0, 0, 0, 0)), 11668);
    addRegion(surface("Ruins of Camdozaal", 0, 0, fromBounds(0, 0, 0, 0)), 11609, 11610, 11611, 11865, 11866, 11867,
        12121, 12122, 12123);
    addRegion(surface("Salt Mine", 0, 0, fromBounds(0, 0, 0, 0)), 11425);
    addRegion(surface("Saradomin Shrine (Paterdomus))", 0, 0, fromBounds(0, 0, 0, 0)), 13722);
    addRegion(surface("Shade Catacombs", 0, 0, fromBounds(0, 0, 0, 0)), 13975);
    addRegion(surface("Shadow Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10575, 10831);
    addRegion(surface("Shayzien Crypts", 0, 0, fromBounds(0, 0, 0, 0)), 6043);
    addRegion(surface("Sisterhood Sanctuary", 0, 0, fromBounds(0, 0, 0, 0)), 14999, 15000, 15001, 15255, 15256, 15257,
        15511, 15512, 15513);
    addRegion(surface("Smoke Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 12946, 13202, 12690);
    addRegion(surface("Sophanem Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 13200);
    addRegion(surface("Sourhog Cave", 0, 0, fromBounds(0, 0, 0, 0)), 12695);
    addRegion(surface("Stronghold of Security", 0, 0, fromBounds(0, 0, 0, 0)), 7505, 8017, 8530, 9297);
    addRegion(surface("Stronghold Slayer Cave", 0, 0, fromBounds(0, 0, 0, 0)), 9624, 9625, 9880, 9881);
    addRegion(surface("Tarn's Lair", 0, 0, fromBounds(0, 0, 0, 0)), 12616, 12615);
    addRegion(surface("Taverley Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 11416, 11417, 11671, 11672, 11673, 11928,
        11929);
    addRegion(surface("Temple of Ikov", 0, 0, fromBounds(0, 0, 0, 0)), 10649, 10905, 10650);
    addRegion(surface("Temple of Light", 0, 0, fromBounds(0, 0, 0, 0)), 7496);
    addRegion(surface("Temple of Marimbo", 0, 0, fromBounds(0, 0, 0, 0)), 11151);
    addRegion(surface("The Warrens", 0, 0, fromBounds(0, 0, 0, 0)), 7070, 7326);
    addRegion(surface("Dungeon of Tolna", 0, 0, fromBounds(0, 0, 0, 0)), 13209);
    addRegion(surface("Tower of Life Basement", 0, 0, fromBounds(0, 0, 0, 0)), 12100);
    addRegion(surface("Trahaearn Mine", 0, 0, fromBounds(0, 0, 0, 0)), 13250);
    addRegion(surface("Tunnel of Chaos", 0, 0, fromBounds(0, 0, 0, 0)), 12625);
    addRegion(surface("Underground Pass", 0, 0, fromBounds(0, 0, 0, 0)), 9369, 9370);
    addRegion(surface("Varrock Sewers", 0, 0, fromBounds(0, 0, 0, 0)), 12954, 13210);
    addRegion(surface("Viyeldi Caves", 0, 0, fromBounds(0, 0, 0, 0)), 9545, 11153);
    addRegion(surface("Warriors' Guild Basement", 0, 0, fromBounds(0, 0, 0, 0)), 11675);
    addRegion(surface("Water Ravine", 0, 0, fromBounds(0, 0, 0, 0)), 13461);
    addRegion(surface("Waterbirth Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 9886, 10142, 7492, 7748);
    addRegion(surface("Waterfall Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10394);
    addRegion(surface("Werewolf Agility Course", 0, 0, fromBounds(0, 0, 0, 0)), 14234);
    addRegion(surface("White Wolf Mountain Caves", 0, 0, fromBounds(0, 0, 0, 0)), 11418, 11419);
    addRegion(surface("Witchhaven Shrine Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10903);
    addRegion(surface("Wizards' Tower Basement", 0, 0, fromBounds(0, 0, 0, 0)), 12437);
    addRegion(surface("Woodcutting Guild Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 6298);
    addRegion(surface("Wyvern Cave", 0, 0, fromBounds(0, 0, 0, 0)), 14495, 14496);
    addRegion(surface("Yanille Agility Dungeon", 0, 0, fromBounds(0, 0, 0, 0)), 10388);
    addRegion(surface("Poison Waste Underground", 0, 0, fromBounds(0, 0, 0, 0)), 5954);

    // Minigames
    addRegion(surface("Ardougne Rat Pits", 0, 0, fromBounds(0, 0, 0, 0)), 10646);
    addRegion(surface("Barbarian Assault", 0, 0, fromBounds(0, 0, 0, 0)), 7508, 7509, 10322);
    addRegion(surface("Barrows", 0, 0, fromBounds(0, 0, 0, 0)), 14131, 14231);
    addRegion(surface("Blast Furnace", 0, 0, fromBounds(0, 0, 0, 0)), 7757);
    addRegion(surface("Brimhaven Agility Arena", 0, 0, fromBounds(0, 0, 0, 0)), 11157);
    addRegion(surface("Burthorpe Games Room", 0, 0, fromBounds(0, 0, 0, 0)), 8781);
    addRegion(surface("Castle Wars", 0, 0, fromBounds(0, 0, 0, 0)), 9520, 9620);
    addRegion(surface("Clan Wars", 0, 0, fromBounds(0, 0, 0, 0)), 12621, 12622, 12623, 13130, 13131, 13133, 13134,
        13135, 13386, 13387, 13390, 13641, 13642, 13643, 13644, 13645, 13646, 13647, 13899, 13900, 14155, 14156);
    addRegion(surface("PvP Arena", 0, 0, fromBounds(0, 0, 0, 0)), 13362, 13363);
    addRegion(surface("Fishing Trawler", 0, 0, fromBounds(0, 0, 0, 0)), 7499, 8011);
    addRegion(surface("The Gauntlet", 0, 0, fromBounds(0, 0, 0, 0)), 12127, 7512);
    addRegion(surface("Corrupted Gauntlet", 0, 0, fromBounds(0, 0, 0, 0)), 7768);
    addRegion(surface("Hallowed Sepulchre", 0, 0, fromBounds(0, 0, 0, 0)), 8797, 9051, 9052, 9053, 9054, 9309, 9563,
        9565, 9821, 10074, 10075, 10077);
    addRegion(surface("The Inferno", 0, 0, fromBounds(0, 0, 0, 0)), 9043);
    addRegion(surface("Keldagrim Rat Pits", 0, 0, fromBounds(0, 0, 0, 0)), 7753);
    addRegion(surface("LMS - Deserted Island", 0, 0, fromBounds(0, 0, 0, 0)), 13658, 13659, 13660, 13914, 13915, 13916);
    addRegion(surface("LMS - Wild Varrock", 0, 0, fromBounds(0, 0, 0, 0)), 13918, 13919, 13920, 14174, 14175, 14176,
        14430, 14431, 14432);
    addRegion(surface("Mage Training Arena", 0, 0, fromBounds(0, 0, 0, 0)), 13462, 13463);
    addRegion(surface("Nightmare Zone", 0, 0, fromBounds(0, 0, 0, 0)), 9033);
    addRegion(surface("Pest Control", 0, 0, fromBounds(0, 0, 0, 0)), 10536);
    addRegion(surface("Port Sarim Rat Pits", 0, 0, fromBounds(0, 0, 0, 0)), 11926);
    addRegion(surface("Pyramid Plunder", 0, 0, fromBounds(0, 0, 0, 0)), 7749);
    addRegion(surface("Rogues' Den", 0, 0, fromBounds(0, 0, 0, 0)), 11854, 11855, 12109, 12110, 12111);
    addRegion(surface("Sorceress's Garden", 0, 0, fromBounds(0, 0, 0, 0)), 11605);
    addRegion(surface("Soul Wars", 0, 0, fromBounds(0, 0, 0, 0)), 8493, 8748, 8749, 9005);
    addRegion(surface("Temple Trekking", 0, 0, fromBounds(0, 0, 0, 0)), 8014, 8270, 8256, 8782, 9038, 9294, 9550, 9806);
    addRegion(surface("Tithe Farm", 0, 0, fromBounds(0, 0, 0, 0)), 7222);
    addRegion(surface("Trouble Brewing", 0, 0, fromBounds(0, 0, 0, 0)), 15150);
    addRegion(surface("Tzhaar Fight Caves", 0, 0, fromBounds(0, 0, 0, 0)), 9551);
    addRegion(surface("Tzhaar Fight Pits", 0, 0, fromBounds(0, 0, 0, 0)), 9552);
    addRegion(surface("Varrock Rat Pits", 0, 0, fromBounds(0, 0, 0, 0)), 11599);
    addRegion(surface("Volcanic Mine", 0, 0, fromBounds(0, 0, 0, 0)), 15263, 15262);
    addRegion(surface("Guardians of the Rift", 0, 0, fromBounds(0, 0, 0, 0)), 14484);
    addRegion(surface("Giant's Foundry", 0, 0, fromBounds(0, 0, 0, 0)), 13491);

    // Raids
    addRegion(surface("Chambers of Xeric", 0, 0, fromBounds(0, 0, 0, 0)), 12889, 13136, 13137, 13138, 13139, 13140,
        13141, 13145, 13393, 13394, 13395, 13396, 13397, 13401);
    addRegion(surface("Theatre of Blood", 0, 0, fromBounds(0, 0, 0, 0)), 12611, 12612, 12613, 12867, 12869, 13122,
        13123, 13125, 13379);
    addRegion(surface("Tombs of Amascut", 0, 0, fromBounds(0, 0, 0, 0)), 14160, 14672, 13454, 14674, 14676, 15184);

    // Other
    addRegion(surface("Home, Sweet Home", 0, 0, fromBounds(0, 0, 0, 0)), 7513);
    addRegion(surface("Old Man's Maze", 0, 0, fromBounds(0, 0, 0, 0)), 11591);
    addRegion(surface("Osmumten's Tomb", 0, 0, fromBounds(0, 0, 0, 0)), 13712, 13456);
    addRegion(surface("Cosmic Entity's Plane", 0, 0, fromBounds(0, 0, 0, 0)), 8267);
    addRegion(surface("Icyene Graveyard", 0, 0, fromBounds(0, 0, 0, 0)), 14641);
    addRegion(surface("Baba Yaga's House", 0, 0, fromBounds(0, 0, 0, 0)), 9800);
    addRegion(surface("Killerwatt Rift", 0, 0, fromBounds(0, 0, 0, 0)), 10577);
    addRegion(surface("Enchanted Valley", 0, 0, fromBounds(0, 0, 0, 0)), 12102);
    addRegion(surface("Essence Mine", 0, 0, fromBounds(0, 0, 0, 0)), 11595);
    addRegion(surface("Sophanem Bank", 0, 0, fromBounds(0, 0, 0, 0)), 11088);
    addRegion(surface("Puro-Puro", 0, 0, fromBounds(0, 0, 0, 0)), 10307);

    // Rune alters
    addRegion(surface("Air Alter", 0, 0, fromBounds(0, 0, 0, 0)), 11339);
    addRegion(surface("Earth Alter", 0, 0, fromBounds(0, 0, 0, 0)), 10571);
    addRegion(surface("Water Alter", 0, 0, fromBounds(0, 0, 0, 0)), 10827);
    addRegion(surface("Fire Alter", 0, 0, fromBounds(0, 0, 0, 0)), 10315);
    addRegion(surface("Mind Alter", 0, 0, fromBounds(0, 0, 0, 0)), 11083);
    addRegion(surface("Body Alter", 0, 0, fromBounds(0, 0, 0, 0)), 10059);
    addRegion(surface("Chaos Alter", 0, 0, fromBounds(0, 0, 0, 0)), 9035);
    addRegion(surface("Death Alter", 0, 0, fromBounds(0, 0, 0, 0)), 8779);
    // addRegion(surface("Blood Alter", 0, 0, fromBounds(0, 0, 0, 0)), 000);
    addRegion(surface("Law Alter", 0, 0, fromBounds(0, 0, 0, 0)), 9803);
    addRegion(surface("Nature Alter", 0, 0, fromBounds(0, 0, 0, 0)), 9547);
    addRegion(surface("Cosmic Alter", 0, 0, fromBounds(0, 0, 0, 0)), 8523);

    // Surface Areas
    addRegion(surface("Zanaris", 0, 0, fromBounds(0, 0, 0, 0)), 9541, 9797, 9540);
    addRegion(surface("Kalphite Caves", 0, 0, fromBounds(0, 0, 0, 0)), 13461, 13460, 13716);
    addRegion(surface("Mage Arena Basement", 0, 0, fromBounds(0, 0, 0, 0)), 10057);
    addRegion(surface("Mage Arena Basement", 0, 0, fromBounds(0, 0, 0, 0)), 10057);

    // Cities
    addRegion(surface("Mor-Ul-Rek", 0, 0, fromBounds(0, 0, 0, 0)), 9808, 10064, 9807, 10069);

    // Events
    addRegion(surface("Mime Show", 0, 0, fromBounds(0, 0, 0, 0)), 8010);

  }
}
