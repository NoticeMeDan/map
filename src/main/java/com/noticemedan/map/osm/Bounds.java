package com.noticemedan.map.osm;

import lombok.Data;

import java.io.Serializable;

@Data
public class Bounds implements Serializable {
	private double minlat;
	private double minlon;
	private double maxlat;
	private double maxlon;
}
