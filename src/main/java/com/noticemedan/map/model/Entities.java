package com.noticemedan.map.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Entities {
	private static double minLat;
	private static double minLon;
	private static double maxLat;
	private static double maxLon;

	public static String writeOut() {
		return "min: {" + minLon + ", " + minLat + "} max: {" + maxLon + ", " + maxLat + "}";
	}
}
