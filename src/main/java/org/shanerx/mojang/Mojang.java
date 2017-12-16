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
public class Mojang {
	
	private Map<String, ServiceStatus> apiStatus;
	
	public void connect() {
		JSONObject obj = getJSONObject("https://status.mojang.com/check");
		obj.forEach((k, v) -> apiStatus.put((String) k, ServiceStatus.valueOf((String) v)));
	}
	
	public ServiceStatus getStatus(ServiceType service) {
		if (service == null) {
			return ServiceStatus.UNKNOWN;
		}
		return apiStatus.get(service.toString());
	}
	
	public String getUUIDOfUsername(String username) {
		return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username).get("id");
	}
	
	public String getUUIDOfUsername(String username, String timestamp) {
		return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + timestamp).get("id");
	}
	
	public Map<String, Long> getNameHistoryOfPlayer(String uuid) {
		JSONArray arr = getJSONArray("https://api.mojang.com/user/profiles/" + uuid + "/names");
		Map<String, Long> history = new HashMap<>();
		arr.forEach(o -> {
			JSONObject obj = (JSONObject) o;
			history.put((String) obj.get("name"), obj.get("changedToAt") == null ? 0 : Long.parseLong(obj.get("changedToAt").toString()));
		});
		return history;
	}
	
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
	
	public void updateSkin(String uuid, String token, SkinType skinType, String skinUrl) {
		try {
			Unirest.post("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).field("model", skinType.toString()).field("url", skinUrl).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	public void updateAndUpload(String uuid, String token, SkinType skinType, String file) {
		try {
			Unirest.put("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).field("model", skinType.toString().equals("") ? "alex" : skinType.toString()).field("file", file).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	public void resetSkin(String uuid, String token) {
		try {
			Unirest.delete("https://api.mojang.com/user/profile/" + uuid + "/skin").header("Authorization", "Bearer " + token).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getServerBlacklist() {
		try {
			return Arrays.asList(Unirest.get("https://sessionserver.mojang.com/blockedservers").asString().getBody().split("\n"));
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
	
	public enum ServiceStatus {
		
		RED,
		YELLOW,
		GREEN,
		UNKNOWN
	}
	
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
		
		@Override
		public String toString() {
			return name().toLowerCase().replace("_", ".");
		}
	}
	
	public enum SkinType {
		DEFAULT,
		SLIM;
		
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