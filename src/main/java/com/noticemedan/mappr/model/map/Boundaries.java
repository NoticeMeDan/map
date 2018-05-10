package com.noticemedan.mappr.model.map;

import lombok.Data;

import java.io.Serializable;

@Data
public class Boundaries implements Serializable {
	private double minLat;
	private double minLon;
	private double maxLat;
	private double maxLon;

	public String writeOut() {
		return "min: {" + this.minLon + ", " + this.minLat + "} max: {" + this.maxLon + ", " + this.maxLat + "}";
	}
}
