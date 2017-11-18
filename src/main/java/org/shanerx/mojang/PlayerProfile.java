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