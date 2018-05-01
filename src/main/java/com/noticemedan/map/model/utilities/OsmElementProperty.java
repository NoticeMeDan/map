package com.noticemedan.map.model.utilities;

import com.noticemedan.map.model.osm.OsmType;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.EnumMap;

@NoArgsConstructor
public class OsmElementProperty {
	private static EnumMap<OsmType, Color> typeColors = new EnumMap<>(OsmType.class);

	public static void colorBlind() {
		typeColors.put(OsmType.COASTLINE, new Color(249, 245, 237));
		typeColors.put(OsmType.WATER, new Color(79, 237, 245));

		typeColors.put(OsmType.GRASSLAND, new Color(168, 236, 122));
		typeColors.put(OsmType.HEATH, new Color(139, 236, 125));
		typeColors.put(OsmType.PARK, new Color(137, 236, 160));
		typeColors.put(OsmType.GARDEN, new Color(14, 236, 15));
		typeColors.put(OsmType.FOREST, new Color(38, 220, 97));

		typeColors.put(OsmType.BUILDING, new Color(238, 110, 59));

		typeColors.put(OsmType.MOTORWAY, new Color(255, 226, 0));
		typeColors.put(OsmType.PRIMARY, new Color(242, 255, 59));
		typeColors.put(OsmType.SECONDARY, new Color(192, 78,201));
		typeColors.put(OsmType.TERTIARY, new Color(230,230,230));
		typeColors.put(OsmType.ROAD, new Color(212, 63, 149));
		typeColors.put(OsmType.FOOTWAY, new Color(174, 53, 255));

		typeColors.put(OsmType.UNKNOWN, new Color(230,0,160));
	}

	public static void standardColour() {
		typeColors.put(OsmType.COASTLINE, new Color(249, 245, 237));
		typeColors.put(OsmType.WATER, new Color(179, 227, 245));

		typeColors.put(OsmType.GRASSLAND, new Color(209, 236, 188));
		typeColors.put(OsmType.HEATH, new Color(209, 236, 188));
		typeColors.put(OsmType.PARK, new Color(209, 236, 188));
		typeColors.put(OsmType.GARDEN, new Color(209, 236, 188));
		typeColors.put(OsmType.FOREST, new Color(171, 220, 160));

		typeColors.put(OsmType.BUILDING, new Color(238,234,226));

		typeColors.put(OsmType.MOTORWAY, new Color(255,205,42));
		typeColors.put(OsmType.PRIMARY, new Color(255,228,141));
		typeColors.put(OsmType.SECONDARY, new Color(201,201,201));
		typeColors.put(OsmType.TERTIARY, new Color(230,230,230));
		typeColors.put(OsmType.ROAD, new Color(212,203,191));
		typeColors.put(OsmType.FOOTWAY, new Color(238,229,217));

		typeColors.put(OsmType.UNKNOWN, new Color(230,0,160));
	}

	public static Color deriveColorFromType(OsmType osmType) {
		Color color = typeColors.get(OsmType.UNKNOWN);
		if (typeColors.get(osmType) != null) color = typeColors.get(osmType);
		return color;
	}
}
