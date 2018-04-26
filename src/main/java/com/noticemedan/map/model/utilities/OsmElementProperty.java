package com.noticemedan.map.model.utilities;

import com.noticemedan.map.model.osm.OSMType;

import java.awt.*;
import java.util.EnumMap;

public class OsmElementProperty {
	private final EnumMap<OSMType, Color> typeColors = new EnumMap<OSMType, Color>(OSMType.class);

	public OsmElementProperty() {
		typeColors.put(OSMType.COASTLINE, new Color(249, 245, 237));
		typeColors.put(OSMType.WATER, new Color(179, 227, 245));

		typeColors.put(OSMType.GRASSLAND, new Color(209, 236, 188));
		typeColors.put(OSMType.HEATH, new Color(209, 236, 188));
		typeColors.put(OSMType.PARK, new Color(209, 236, 188));
		typeColors.put(OSMType.GARDEN, new Color(209, 236, 188));
		typeColors.put(OSMType.FOREST, new Color(171, 220, 160));


		typeColors.put(OSMType.BUILDING, new Color(238,234,226));

		typeColors.put(OSMType.MOTORWAY, new Color(255,205,42));
		typeColors.put(OSMType.PRIMARY, new Color(255,228,141));
		typeColors.put(OSMType.SECONDARY, new Color(201,201,201));
		typeColors.put(OSMType.TERTIARY, new Color(230,230,230));
		typeColors.put(OSMType.ROAD, new Color(212,203,191));
		typeColors.put(OSMType.FOOTWAY, new Color(238,229,217));

		typeColors.put(OSMType.UNKNOWN, new Color(230,0,160));
	}

	public Color deriveColorFromType(OSMType osmType) {
		Color color = this.typeColors.get(OSMType.UNKNOWN);
		if (typeColors.get(osmType) != null) color = this.typeColors.get(osmType);
		return color;
	}
}
