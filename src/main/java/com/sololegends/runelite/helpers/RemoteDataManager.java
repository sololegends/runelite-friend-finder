package com.sololegends.runelite.helpers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import javax.inject.Inject;

import com.google.gson.*;
import com.sololegends.runelite.*;

import net.runelite.api.coords.WorldPoint;
import okhttp3.*;

public class RemoteDataManager {

	private volatile boolean in_progress = false;

	@Inject
	private FriendsOnMapPlugin plugin;

	@Inject
	private FriendsOnMapConfig config;

	@Inject
	private OkHttpClient http_client;

	public void sendRequest(JsonObject data) {
		if (in_progress) {
			return;
		}
		in_progress = true;
		Request.Builder req_builder = new Request.Builder()
				.url(config.friendsAPI())
				.post(RequestBody.create(MediaType.get("application/json"), data.toString()));

		http_client.newCall(req_builder.build()).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				in_progress = false;
				if (e instanceof MalformedURLException) {
					plugin.message("Failed to retrieve friends from friends api: Invalid API URL");
					return;
				}
				plugin.message("Failed to retrieve friends from friends api: API Call Failed");
			}

			@Override
			public void onResponse(Call call, Response resp) throws IOException {
				if (resp.code() != 200) {
					plugin.message("Failed to retrieve friends from friends api [RC2]");
					plugin.updated(System.currentTimeMillis() + (config.updateInterval().interval() * 9));
					in_progress = false;
					return;
				}
				JsonElement arr = new JsonParser().parse(resp.body().string());
				if (arr.isJsonArray()) {
					JsonArray my_friends = arr.getAsJsonArray();
					if (my_friends != null && my_friends.size() > 0) {
						// Remove old points
						plugin.clearPoints();
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
								FriendMapPoint wmp = new FriendMapPoint(
										new WorldPoint(f.get("x").getAsInt(), f.get("y").getAsInt(), f.get("z").getAsInt()),
										plugin.getIcon(!plugin.isCurrentWorld(f.get("w").getAsInt()), false), f.get("name").getAsString(),
										f.get("w").getAsInt());
								wmp.setName(f.get("name").getAsString());
								wmp.setTooltip(f.get("name").getAsString() + " -- World: " + f.get("w").getAsString());
								wmp.setSnapToEdge(true);

								plugin.addPoint(wmp);
							}
						}
					}
				}
				plugin.updated(System.currentTimeMillis());
				in_progress = false;
			}
		});
	}
}
