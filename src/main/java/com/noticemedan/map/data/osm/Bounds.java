package com.noticemedan.map.data.osm;

import lombok.Data;
import org.simpleframework.xml.Attribute;

@Data
public class Bounds {
	@Attribute
	private double minlat;

	@Attribute
	private double minlon;

	@Attribute
	private double maxlat;

	@Attribute
	private double maxlon;
}
