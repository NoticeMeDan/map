package com.noticemedan.mappr.model.map;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Boundaries implements Serializable {
	@Getter @Setter
	private double minLat;
	@Getter @Setter
	private double minLon;
	@Getter @Setter
	private double maxLat;
	@Getter @Setter
	private double maxLon;

	public String writeOut() {
		return "min: {" + this.minLon + ", " + this.minLat + "} max: {" + this.maxLon + ", " + this.maxLat + "}";
	}
}
