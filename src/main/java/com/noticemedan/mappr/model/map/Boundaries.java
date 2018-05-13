package com.noticemedan.mappr.model.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Boundaries implements Serializable {
	private double minLat;
	private double minLon;
	private double maxLat;
	private double maxLon;

	public String writeOut() {
		return "min: {" + this.minLon + ", " + this.minLat + "} max: {" + this.maxLon + ", " + this.maxLat + "}";
	}
}
