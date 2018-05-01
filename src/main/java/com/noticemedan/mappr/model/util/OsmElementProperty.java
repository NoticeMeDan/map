package com.noticemedan.mappr.model.util;

import com.noticemedan.mappr.model.osm.Type;

import java.awt.*;
import java.util.EnumMap;

public class OsmElementProperty {
	private final EnumMap<Type, Color> typeColors = new EnumMap<Type, Color>(Type.class);

	public OsmElementProperty() {
		typeColors.put(Type.COASTLINE, new Color(249, 245, 237));
		typeColors.put(Type.WATER, new Color(179, 227, 245));

		typeColors.put(Type.GRASSLAND, new Color(209, 236, 188));
		typeColors.put(Type.HEATH, new Color(209, 236, 188));
		typeColors.put(Type.PARK, new Color(209, 236, 188));
		typeColors.put(Type.GARDEN, new Color(209, 236, 188));
		typeColors.put(Type.FOREST, new Color(171, 220, 160));


		typeColors.put(Type.BUILDING, new Color(238,234,226));

		typeColors.put(Type.MOTORWAY, new Color(255,205,42));
		typeColors.put(Type.PRIMARY, new Color(255,228,141));
		typeColors.put(Type.SECONDARY, new Color(201,201,201));
		typeColors.put(Type.TERTIARY, new Color(230,230,230));
		typeColors.put(Type.ROAD, new Color(212,203,191));
		typeColors.put(Type.FOOTWAY, new Color(238,229,217));

		typeColors.put(Type.UNKNOWN, new Color(230,0,160));
	}

	public Color deriveColorFromType(Type type) {
		Color color = this.typeColors.get(Type.UNKNOWN);
		if (typeColors.get(type) != null) color = this.typeColors.get(type);
		return color;
	}
}
