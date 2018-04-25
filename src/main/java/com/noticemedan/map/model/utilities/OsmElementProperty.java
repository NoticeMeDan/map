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
		typeColors.put(OSMType.TREE_ROW, new Color(15, 130, 80));
		typeColors.put(OSMType.HEATH, new Color(209, 236, 188));
		typeColors.put(OSMType.ROAD, new Color(125,125,125));
		typeColors.put(OSMType.HIGHWAY, new Color(200,100,0));
		typeColors.put(OSMType.PRIMARY, new Color(255,227,133));
		typeColors.put(OSMType.BUILDING, new Color(150,150,150));
		typeColors.put(OSMType.UNKNOWN, new Color(230,0,160));
	}

	public Color deriveColorFromType(OSMType osmType) {
		Color color = this.typeColors.get(OSMType.UNKNOWN);
		if (typeColors.get(osmType) != null) color = this.typeColors.get(osmType);
		return color;
	}
}
