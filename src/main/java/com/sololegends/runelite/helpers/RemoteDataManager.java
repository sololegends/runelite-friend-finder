package com.sololegends.runelite.helpers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import javax.inject.Inject;

import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;
import com.sololegends.runelite.*;
import com.sololegends.runelite.data.WorldRegions;
import com.sololegends.runelite.helpers.WorldLocations.WorldSurface;
import com.sololegends.runelite.skills.Health;
import com.sololegends.runelite.skills.Prayer;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import okhttp3.*;

@Slf4j
public class RemoteDataManager {

  private volatile boolean in_progress = false;
  private volatile boolean report_in_progress = false;
  private List<FriendMapPoint> FAKE_FRIENDS = new ArrayList<>();
  private Random rand = new Random();

  @Inject
  private FriendsOnMapPlugin plugin;

  @Inject
  private FriendsOnMapConfig config;

  @Inject
  private OkHttpClient http_client;

  private int errors = 0;
  private int errors_threshold = 10;
  private boolean error_notified = false;

  public void sendReport(JsonObject data, UpdateFlow update) {
    if (report_in_progress) {
      return;
    }
    report_in_progress = true;
    try {
      Request.Builder req_builder = new Request.Builder()
          .url(config.reportLink())
          .post(RequestBody.create(MediaType.get("application/json"), data.toString()));

      // Handle APi key, if present
      if (!config.friendsAPIKey().isBlank()) {
        req_builder.header("Authorization", "Bearer " + config.friendsAPIKey());
      }

      http_client.newCall(req_builder.build()).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
          update.error("Failed to call API");
          report_in_progress = false;
        }

        @Override
        public void onResponse(Call call, Response resp) throws IOException {
          if (resp.body() != null) {
            resp.body().close();
          }
          if (resp.code() == 200) {
            update.success("Location reported!");
            report_in_progress = false;
            return;
          }
          update.error("Failed to report!");
          report_in_progress = false;
        }
      });
    } catch (IllegalArgumentException e) {
      update.error("Config Error!");
    }
  }

  public void getServerLocations() {
    // Is disabled by config
    if (config.locationsLink() == null || config.locationsLink().isBlank()) {
      return;
    }
    try {
      Request.Builder req_builder = new Request.Builder()
          .url(config.locationsLink())
          .get();

      // Handle APi key, if present
      if (!config.friendsAPIKey().isBlank()) {
        req_builder.header("Authorization", "Bearer " + config.friendsAPIKey());
      }

      http_client.newCall(req_builder.build()).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response resp) throws IOException {
          if (resp.body() == null) {
            return;
          }

          if (resp.code() != 200) {
            log.error("FFP => Failed to retrieve server registered locations [" + resp.code() + "]");
            return;
          }
          try {
            JsonElement arr = new JsonParser().parse(resp.body().string());
            // Process retrieved locations
            if (arr.isJsonArray()) {
              JsonArray locations = arr.getAsJsonArray();
              if (locations != null && locations.size() > 0) {
                // Add new points
                Iterator<JsonElement> iter = locations.iterator();
                while (iter.hasNext()) {
                  JsonElement e = iter.next();
                  if (!e.isJsonObject()) {
                    continue;
                  }

                  JsonObject f = e.getAsJsonObject();
                  // Validate the friend object
                  try {
                    if (f.has("x") && f.has("y") && f.has("z") &&
                        f.has("location") && f.has("region") && f.has("instance")) {

                      int x = f.get("x").getAsInt();
                      int y = f.get("y").getAsInt();
                      int region = f.get("region").getAsInt();
                      int instance = f.get("instance").getAsInt();
                      if (instance != -1) {
                        region = instance;
                      }
                      String location = f.get("location").getAsString();

                      WorldRegions.addRegion(
                          WorldRegions.surface(location, x, y, WorldRegions.fromBounds(0, 0, 0, 0)),
                          !config.debugMode(),
                          region);
                    }
                  } catch (ClassCastException cce) {
                    // Nothing, just ignore the location
                  }

                }
              }

            }
          } catch (MalformedJsonException e) {
            log.error("FFP => Failed to retrieve server registered locations");
            e.printStackTrace();
          }
        }
      });
    } catch (IllegalArgumentException e) {
      log.error("FFP => Failed to retrieve server registered locations");
      e.printStackTrace();
    }
  }

  public void sendRequest(JsonObject data) {
    if (in_progress) {
      return;
    }
    in_progress = true;
    Request.Builder req_builder = null;
    try {
      req_builder = new Request.Builder()
          .url(config.friendsAPI())
          .post(RequestBody.create(MediaType.get("application/json"), data.toString()));

      // Handle APi key, if present
      if (!config.friendsAPIKey().isBlank()) {
        req_builder.header("Authorization", "Bearer " + config.friendsAPIKey());
      }
    } catch (IllegalArgumentException e) {
      log.error("Failed to setup url: " + e.getMessage());
      e.printStackTrace();
      return;
    }
    if (req_builder == null) {
      return;
    }

    http_client.newCall(req_builder.build()).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        in_progress = false;
        if (e instanceof MalformedURLException) {
          plugin.message("Failed to retrieve friends from friends api: Invalid API URL");
          return;
        }
        // plugin.message("Failed to retrieve friends from friends api: API Call
        // Failed");
      }

      @Override
      public void onResponse(Call call, Response resp) throws IOException {
        if (resp.code() != 200 && !error_notified) {
          if (resp.body() != null) {
            resp.body().close();
          }
          if (errors < errors_threshold) {
            errors++;
          }
          errors = 0;
          error_notified = true;
          plugin.message("Failed to retrieve friends from friends api [RC2]");
          plugin.updated(System.currentTimeMillis() + (config.updateInterval().interval() * 9));
          in_progress = false;
          return;
        }
        try {
          JsonElement arr = new JsonParser().parse(resp.body().string());
          if (arr.isJsonArray()) {
            JsonArray my_friends = arr.getAsJsonArray();
            if (my_friends != null && my_friends.size() > 0) {
              // Add new points
              Iterator<JsonElement> iter = my_friends.iterator();
              while (iter.hasNext()) {
                JsonElement e = iter.next();
                if (!e.isJsonObject()) {
                  continue;
                }
                JsonObject f = e.getAsJsonObject();
                // Validate the friend object
                if (f.has("x") && f.has("y") && f.has("z") &&
                    f.has("name") && f.has("w")) {

                  String tool_tip = f.get("name").getAsString() + " -- World: " + f.get("w").getAsString();
                  FriendMapPoint wmp = new FriendMapPoint(
                      new WorldPoint(f.get("x").getAsInt(), f.get("y").getAsInt(), f.get("z").getAsInt()),
                      plugin.getIcon(!plugin.isCurrentWorld(f.get("w").getAsInt()), false, tool_tip,
                          false),
                      f.get("name").getAsString(),
                      f.get("w").getAsInt()) {
                    @Override
                    public void onEdgeSnap() {
                      super.onEdgeSnap();
                      plugin.updateFriendPointIcon(this, true);
                    }

                    @Override
                    public void onEdgeUnsnap() {
                      super.onEdgeUnsnap();
                      plugin.updateFriendPointIcon(this, true);
                    }
                  };

                  int ds = config.dotSize();
                  wmp.setImagePoint(new Point(ds / 2, ds / 2));
                  wmp.setName(f.get("name").getAsString());
                  wmp.setTooltip(tool_tip);
                  wmp.setSnapToEdge(true);
                  if (f.has("r")) {
                    wmp.setRegion(f.get("r").getAsInt());
                  }

                  if (f.has("l") && !f.get("l").isJsonNull()) {
                    WorldPoint entry = new WorldPoint(0, 0, 0);
                    if (f.has("lx") && f.has("ly")) {
                      int lx = f.get("lx").getAsInt();
                      int ly = f.get("ly").getAsInt();
                      int lp = f.has("lp") ? f.get("lp").getAsInt() : 0;
                      entry = new WorldPoint(lx, ly, lp);
                    }
                    WorldSurface loc = new WorldSurface(f.get("l").getAsString(), entry, new WorldArea(0, 0, 0, 0, 0));
                    wmp.setLocation(loc);
                  }

                  // If health set
                  if (f.has("hm") && f.get("hm").getAsInt() != -1 && f.has("hM") && f.get("hM").getAsInt() != -1) {
                    wmp.setHealth(new Health(f.get("hm").getAsInt(), f.get("hM").getAsInt()));
                  }

                  // If prayer set
                  if (f.has("pm") && f.get("pm").getAsInt() != -1 && f.has("pM") && f.get("pM").getAsInt() != -1) {
                    wmp.setPrayer(new Prayer(f.get("pm").getAsInt(), f.get("pM").getAsInt()));
                  }

                  plugin.addPoint(wmp);
                }
              }
            }
          }

        } catch (JsonSyntaxException e) {
          log.error("FFP => Malformed JSON from server, likely offline");
          in_progress = false;
        }
        // * If we need to render in some fake friends
        if (config.fakeFriends()) {
          int x = 1052, y2 = 4132, x2 = 3940, y = 2396;
          // Generate a surface world wo
          if (FAKE_FRIENDS.size() == 0) {
            // Generate some friends
            int count = rand.nextInt(15) + 20;
            for (int i = 0; i < count; i++) {
              String name = "Fake Friend #" + i;
              String tool_tip = name + " -- World: 000";
              FriendMapPoint fmp = new FriendMapPoint(
                  new WorldPoint(rand.nextInt(x2 - x) + x, rand.nextInt(y2 - y) + y, 0),
                  plugin.getIcon(!plugin.isCurrentWorld(0), false, tool_tip,
                      false),
                  name, 0);
              int ds = config.dotSize();
              fmp.setImagePoint(new Point(ds / 2, ds / 2));
              fmp.setName(name);
              fmp.setTooltip(tool_tip);
              fmp.setSnapToEdge(true);
              FAKE_FRIENDS.add(fmp);
            }
          }
          // Process moving those friends around a bit
          for (FriendMapPoint mp : FAKE_FRIENDS) {
            WorldPoint wp = mp.getWorldPoint();
            int nx = wp.getX() + (rand.nextInt(20) - 10);
            if (nx < x) {
              nx = x;
            }
            if (nx > x2) {
              nx = x2;
            }
            int ny = wp.getY() + (rand.nextInt(20) - 10);
            if (ny < y) {
              ny = y;
            }
            if (ny > y2) {
              ny = y2;
            }
            mp.setWorldPoint(new WorldPoint(nx, ny, wp.getPlane()));
            plugin.addPoint(mp);
          }
        } else {
          FAKE_FRIENDS.clear();
        }
        plugin.updatePanel();
        plugin.updated(System.currentTimeMillis());
        in_progress = false;
      }
    });
  }

  public abstract static class UpdateFlow {
    public abstract void success(String message);

    public abstract void error(String message);
  }
}
