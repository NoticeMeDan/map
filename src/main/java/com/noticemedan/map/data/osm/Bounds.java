package com.noticemedan.map.data.osm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
