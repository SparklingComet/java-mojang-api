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

@SuppressWarnings("unused")
public class PlayerProfile {

	private String uuid;
	private String username;
	private Set<Property> properties;
	
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
	
	public PlayerProfile(String uuid, String username, Set<Property> properties) {
		this.uuid = uuid;
		this.username = username;
		this.properties = properties;
	}
}