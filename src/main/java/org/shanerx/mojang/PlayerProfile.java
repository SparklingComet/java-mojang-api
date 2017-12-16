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

import java.util.Set;

/**
 * This class contains the fields that represent the metadata of a player account and the methods to interact with it.
 */
@SuppressWarnings("unused")
public class PlayerProfile {

	private String uuid;
	private String username;
	private Set<Property> properties;

	/**
	 * Represents a property.
	 */
	public static class Property {
		String name;
		String value;
		String signature;
		
		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getSignature() {
			return signature;
		}
	}

	/**
	 * <p>Constructor for the class.
	 * <p>You may use {@code new Mojang().connect().getPlayerProfile(uuid)} to retrieve the instance as it will verify the validity of the parameters.
	 *
	 * @param uuid the UUID of the player this object should represent
	 * @param username the username of said player (you may use {@code new Mojang().connect().getNameHistoryOfPlayer(uuid)} to retrieve it).
	 * @param properties the properties for that player. Depends on what you wish to do with the object
	 */
	public PlayerProfile(String uuid, String username, Set<Property> properties) {
		this.uuid = uuid;
		this.username = username;
		this.properties = properties;
	}

	/**
	 * Gets the UUID of the player.
	 *
	 * @return the uuid as a {@link java.lang.String String}
	 */
	public String getUUID() {
		return uuid;
	}

	/**
	 * Gets the username of the player.
	 *
	 * @return the username as a {@link java.lang.String String}
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * <p>Returns the properties this object has.
	 * <p>This method exists for transparency, as the properties set is used internally.
	 *
	 * @return the properties {@link java.util.Set Set}
	 */
	public Set<Property> getProperties() {
		return properties;
	}
}