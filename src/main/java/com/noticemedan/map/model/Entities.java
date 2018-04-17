package com.noticemedan.map.model;


import lombok.Getter;
import lombok.Setter;

public class Entities {
	@Getter @Setter
	private static double minLat;
	@Getter @Setter
	private static double minLon;
	@Getter @Setter
	private static double maxLat;
	@Getter @Setter
	private static double maxLon;

	public static String writeOut() {
		return "min: {" + minLon + ", " + minLat + "} max: {" + maxLon + ", " + maxLat + "}";
	}
}
