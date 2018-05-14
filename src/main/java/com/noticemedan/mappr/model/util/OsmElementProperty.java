package com.noticemedan.mappr.model.util;

import com.noticemedan.mappr.model.map.Type;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.EnumMap;

@NoArgsConstructor
public class OsmElementProperty {
	private static EnumMap<Type, Color> typeColors = new EnumMap<>(Type.class);

	public static void colorBlind() {
		typeColors.put(Type.COASTLINE, new Color(182, 183, 182));
		typeColors.put(Type.WATER, new Color(79, 237, 245));

		typeColors.put(Type.GRASSLAND, new Color(7, 236, 0));
		typeColors.put(Type.HEATH, new Color(229, 236, 0));
		typeColors.put(Type.PARK, new Color(0, 255, 171));
		typeColors.put(Type.GARDEN, new Color(22, 135, 0));
		typeColors.put(Type.FOREST, new Color(154, 255, 168));

		typeColors.put(Type.BUILDING, new Color(149, 238, 236));
		typeColors.put(Type.RESIDENTIAL, new Color(2, 111, 187));

		typeColors.put(Type.AERODROME, new Color(83, 10, 132));
		typeColors.put(Type.TAXIWAY, new Color(215, 220, 224));
		typeColors.put(Type.RUNWAY, new Color(203, 6, 0));
		typeColors.put(Type.RAIL, new Color(208, 208, 208));

		typeColors.put(Type.MOTORWAY, new Color(129, 0, 29));
		typeColors.put(Type.PRIMARY, new Color(255, 177, 0));
		typeColors.put(Type.SECONDARY, new Color(192, 78,201));
		typeColors.put(Type.TERTIARY, new Color(230, 0, 9));
		typeColors.put(Type.ROAD, new Color(212, 63, 149));
		typeColors.put(Type.FOOTWAY, new Color(174, 53, 255));
		typeColors.put(Type.TRACK, new Color(201, 0, 136));
		typeColors.put(Type.SERVICE, new Color(201,201,201));
		typeColors.put(Type.CYCLEWAY, new Color(145,201, 0));
		typeColors.put(Type.UNCLASSIFIED, new Color(201, 187, 24));
		typeColors.put(Type.PATH, new Color(255, 27, 128));

		typeColors.put(Type.UNKNOWN, new Color(230,0,160));
	}

	public static void standardColor() {
		typeColors.put(Type.COASTLINE, new Color(249, 245, 237));
		typeColors.put(Type.WATER, new Color(179, 227, 245));

		typeColors.put(Type.GRASSLAND, new Color(209, 236, 188));
		typeColors.put(Type.HEATH, new Color(209, 236, 188));
		typeColors.put(Type.PARK, new Color(209, 236, 188));
		typeColors.put(Type.GARDEN, new Color(209, 236, 188));
		typeColors.put(Type.FOREST, new Color(171, 220, 160));

		typeColors.put(Type.BUILDING, new Color(238,234,226));

		typeColors.put(Type.AERODROME, new Color(225, 230, 235));
		typeColors.put(Type.TAXIWAY, new Color(215, 220, 224));
		typeColors.put(Type.RUNWAY, new Color(215, 220, 224));
		typeColors.put(Type.RAIL, new Color(208, 208, 208));

		typeColors.put(Type.MOTORWAY, new Color(255, 207, 22));
		typeColors.put(Type.MOTORWAY_LINK, new Color(255, 207, 22));
		typeColors.put(Type.PRIMARY, new Color(255,228,141));
		typeColors.put(Type.TRUNK, new Color(255, 226, 104));
		typeColors.put(Type.SECONDARY, new Color(201,201,201));
		typeColors.put(Type.TERTIARY, new Color(230,230,230));
		typeColors.put(Type.ROAD, new Color(212,203,191));
		typeColors.put(Type.RESIDENTIAL, new Color(212,203,191));
		typeColors.put(Type.FOOTPATH, new Color(238,229,217));
		typeColors.put(Type.FOOTWAY, new Color(238,229,217));
		typeColors.put(Type.PATH, new Color(238,229,217));
		typeColors.put(Type.PEDESTRIAN, new Color(238,229,217));
		typeColors.put(Type.TRACK, new Color(238, 229, 217));
		typeColors.put(Type.SERVICE, new Color(212,203,191));
		typeColors.put(Type.UNCLASSIFIED, new Color(212,203,191));
		typeColors.put(Type.CYCLEWAY, new Color(212, 203, 191));

		typeColors.put(Type.UNKNOWN, new Color(230,0,160));
	}

	public static Color deriveColorFromType(Type osmType) {
		Color color = typeColors.get(Type.UNKNOWN);
		if (typeColors.get(osmType) != null) color = typeColors.get(osmType);
		return color;
	}
}
