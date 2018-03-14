package com.noticemedan.map.osm;

import lombok.Data;

@Data
public class Bounds {
	private double minlat;
	private double minlon;
	private double maxlat;
	private double maxlon;
}
