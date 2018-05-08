package com.noticemedan.mappr.model.util;

import com.noticemedan.mappr.model.map.Type;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.EnumMap;

@NoArgsConstructor
public class OsmElementProperty {
	private static EnumMap<Type, Color> typeColors = new EnumMap<>(Type.class);

	public static void colorBlind() {
		typeColors.put(Type.COASTLINE, new Color(249, 245, 237));
		typeColors.put(Type.WATER, new Color(79, 237, 245));

		typeColors.put(Type.GRASSLAND, new Color(168, 236, 122));
		typeColors.put(Type.HEATH, new Color(139, 236, 125));
		typeColors.put(Type.PARK, new Color(137, 236, 160));
		typeColors.put(Type.GARDEN, new Color(14, 236, 15));
		typeColors.put(Type.FOREST, new Color(38, 220, 97));

		typeColors.put(Type.BUILDING, new Color(238, 110, 59));

		typeColors.put(Type.MOTORWAY, new Color(255, 226, 0));
		typeColors.put(Type.PRIMARY, new Color(242, 255, 59));
		typeColors.put(Type.SECONDARY, new Color(192, 78,201));
		typeColors.put(Type.TERTIARY, new Color(230,230,230));
		typeColors.put(Type.ROAD, new Color(212, 63, 149));
		typeColors.put(Type.FOOTWAY, new Color(174, 53, 255));

		typeColors.put(Type.UNKNOWN, new Color(230,0,160));
	}

	public static void standardColour() {
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

	public static Color deriveColorFromType(Type osmType) {
		Color color = typeColors.get(Type.UNKNOWN);
		if (typeColors.get(osmType) != null) color = typeColors.get(osmType);
		return color;
	}
}
