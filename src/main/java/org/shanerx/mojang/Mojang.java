/*
 *     Copyright 2016-2017 SparklingComet @ http://shanerx.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.shanerx.mojang;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "unchecked"})
/**
 * <p>This class represents the connection with the Mojang API.
 * <p>All instances of other classes of this wrapper API should be retrieved through this class.
 * <p>Remember to call <code>api.connect()</code> after creating an instance of this class.
 */
public class Mojang {
	
	private Map<String, ServiceStatus> apiStatus;

	/**
	 * Constructor. Initializes member variables.
	 */
	public Mojang() {
		apiStatus = new HashMap<>();
	}

	/**
	 * <p>Opens the connection with the Mojang API.
	 * Should <strong>always</strong> be called after creating the API object.
	 *
	 * <p><strong>Example:</strong>
	 * <code>Mojang api = new Mojang().connect();</code>
	 *
	 * @return the api itself. Useful for concatenation.
	 */
	public Mojang connect() {
		JSONObject obj = getJSONObject("https://status.mojang.com/check");
		obj.forEach((k, v) -> apiStatus.put((String) k, ServiceStatus.valueOf((String) v)));
		return this;
	}

	/**
	 * Retrieves the {@link org.shanerx.mojang.Mojang.ServiceStatus status} of a portion of the API.
	 * Check the enum entries for {@link org.shanerx.mojang.Mojang.ServiceStatus} for possible response types.
	 *
	 * @param service the service type
	 * @return the status of said service
	 */
	public ServiceStatus getStatus(ServiceType service) {
		if (service == null) {
			return ServiceStatus.UNKNOWN;
		}
		return apiStatus.get(service.toString());
	}

	/**
	 * Retrieves the current UUID linked to a username.
	 *
	 *  @param username the username
	 * @return the UUID as a {@link java.lang.String String}
	 */
	public String getUUIDOfUsername(String username) {
		return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username).get("id");
	}

	/**
	 * Retrieves the UUID linked to a username at a certain moment in time.
	 *
	 * @param username the username
	 * @param timestamp the Java Timestamp that represents the time
	 * @return the UUID as a {@link java.lang.String String}
	 */
	public String getUUIDOfUsername(String username, String timestamp) {
		return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + timestamp).get("id");
	}

	/**
	 * Retrieves all the username a certain UUID has had in the past, including the current one.
	 * @param uuid the UUID
	 * @return a map with the username as key value and the Timestamp as a {@link java.lang.Long Long}
	 */
	public Map<String, Long> getNameHistoryOfPlayer(String uuid) {
		JSONArray arr = getJSONArray("https://api.mojang.com/user/profiles/" + uuid + "/names");
		Map<String, Long> history = new HashMap<>();
		arr.forEach(o -> {
			JSONObject obj = (JSONObject) o;
			history.put((String) obj.get("name"), obj.get("changedToAt") == null ? 0 : Long.parseLong(obj.get("changedToAt").toString()));
		});
		return history;
	}

	/**
	 * Returns the {@link org.shanerx.mojang.PlayerProfile PlayerProfile} object which holds and represents the metadata for a certain account.
	 *
	 * @param uuid the UUID of the player
	 * @return the {@link org.shanerx.mojang.PlayerProfile PlayerProfile} object}
	 */
	public PlayerProfile getPlayerProfile(String uuid) {
		JSONObject obj = getJSONObject("https://sessionserver.mojang.com/session/minecraft/profile/<uuid>");
		String name = (String) obj.get("name");
		Set<PlayerProfile.Property> properties = (Set<PlayerProfile.Property>) ((JSONArray) obj.get("properties")).stream().map(o -> {
			PlayerProfile.Property p = new PlayerProfile.Property();
			JSONObject prop = (JSONObject) o;
			p.name = (String) prop.get("name");
			p.signature = (String) prop.get("signature");
			p.value = (String) prop.get("value");
			return p;
		}).collect(Collectors.toSet());
		return new PlayerProfile(uuid, name, properties);
	}

	/**
	 * Updates the skin of a player using a URI.
	 * This means that the image file will <strong>not</strong> be uploaded to Mojang's servers, hence the API will need to query the given URI.
	 *
	 * @param uuid the UUID of said player
	 * @param token the token used for API authentication
	 * @param skinType the {@link org.shanerx.mojang.Mojang.SkinType type} of the skin
	 * @param skinUrl a direct URL to the skin
	 */
	public void updateSkin(String uuid, String token, SkinType skinType, String skinUrl) {
		try {
			Unirest.post("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).field("model", skinType.toString()).field("url", skinUrl).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the skin of a player using a URI.
	 * The raw skin data will be uploaded to Mojang's servers and stored there potentially forever.
	 *
	 * @param uuid the UUID of said player
	 * @param token the token used for API authentication
	 * @param skinType the {@link org.shanerx.mojang.Mojang.SkinType type} of the skin
	 * @param file the raw image data
	 */
	@Untested
	public void updateAndUpload(String uuid, String token, SkinType skinType, String file) {
		try {
			Unirest.put("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).field("model", skinType.toString().equals("") ? "alex" : skinType.toString()).field("file", file).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resets the skin to the default.
	 *
	 * @param uuid the UUID of the player
	 * @param token the token used for API authentication
	 */
	public void resetSkin(String uuid, String token) {
		try {
			Unirest.delete("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>Returns a list of blacklisted hostnames, belonging to servers that were blocked due to Mojang's EULA infringement.
	 * <p><strong>N.B.:</strong> These may not be in human friendly form as they were hashed. You may want to use third-party services to obtain an (unofficial) list.
	 *
	 * @return a {@link java.util.List List} of all the blocked hostnames
	 */
	public List<String> getServerBlacklist() {
		try {
			return Arrays.asList(Unirest.get("https://sessionserver.mojang.com/blockedservers").asString().getBody().split("\n"));
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the official mojang's product sales statistics.
	 *
	 * @param options the query {@link org.shanerx.mojang.SalesStats.Options options}
	 * @return the stats
	 */
	@Untested
	public SalesStats getSaleStatistics(SalesStats.Options...options) {
		JSONArray arr = new JSONArray();
		Collections.addAll(arr, options);
		
		SalesStats stats = null;
		try {
			JSONObject resp = (JSONObject) new JSONParser().parse(Unirest.post("https://api.mojang.com/orders/statistics").field("metricKeys", arr).asString().getBody());
			stats = new SalesStats(Integer.valueOf((String) resp.get("total")), Integer.valueOf((String) resp.get("last24h")), Integer.valueOf((String) resp.get("saleVelocityPerSeconds")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stats;
	}

	/**
	 * This enum represents the possible Mojang API servers availability statuses.
	 */
	public enum ServiceStatus {
		
		RED,
		YELLOW,
		GREEN,
		UNKNOWN
	}

	/**
	 * This enum represents the various portions of the Mojang API.
	 */
	public enum ServiceType {
		
		MINECRAFT_NET,
		SESSION_MINECRAFT_NET,
		ACCOUNT_MOJANG_NET,
		AUTH_MOJANG_COM,
		SKINS_MINECRAFT_NET,
		AUTHSERVER_MOJANG_COM,
		SESSIONSERVER_MOJANG_COM,
		API_MOJANG_COM,
		TEXTURES_MINECRAFT_NET,
		MOJANG_COM;

		/**
		 * <p>This method overrides {@code java.lang.Object.toString()} and returns the address of the mojang api portion a certain enum constant represents.
		 * <p><strong>Example:</strong>
		 * {@code org.shanerx.mojang.Mojang.ServiceType.MINECRAFT_NET.toString()} will return {@literal minecraft.net}
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return name().toLowerCase().replace("_", ".");
		}
	}

	/**
	 * This enum represents the skin types "Alex" and "Steve".
	 */
	public enum SkinType {
		/**
		 * Steve
		 */
		DEFAULT,
		/**
		 * Alex
		 */
		SLIM;

		/**
		 * Returns the query parameter version for these skin types in order to send HTTP requests to the API.
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return this == DEFAULT ? "" : "slim";
		}
	}

	private static JSONObject getJSONObject(String url) {
		JSONObject obj = null;
		try {
			obj = (JSONObject) new JSONParser().parse(Unirest.get(url).asString().getBody());
			String err = (String) (obj.get("error"));
			if (err != null) {
				switch (err) {
					case "IllegalArgumentException":
						throw new IllegalArgumentException((String) obj.get("errorMessage"));
					default:
						throw new RuntimeException();
				}
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		} finally {
			return obj;
		}
	}
	
	private static JSONArray getJSONArray(String url) {
		JSONArray arr = null;
		try {
			arr = (JSONArray) new JSONParser().parse(Unirest.get(url).asString().getBody());
			
		} catch (ParseException e) {
			throw new RuntimeException(e);
			
		} catch (ClassCastException e) {
			JSONObject obj = (JSONObject) new JSONParser().parse(Unirest.get(url).toString());
			String err = (String) (obj.get("error"));
			if (err != null) {
				switch (err) {
					case "IllegalArgumentException":
						throw new IllegalArgumentException((String) obj.get("errorMessage"));
					default:
						throw new RuntimeException();
				}
			}
		} finally {
			return arr;
		}
	}
}