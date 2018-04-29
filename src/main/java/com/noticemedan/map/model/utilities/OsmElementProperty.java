package com.noticemedan.map.model.utilities;

import com.noticemedan.map.model.osm.OsmType;

import java.awt.*;
import java.util.EnumMap;

public class OsmElementProperty {
	private final EnumMap<OsmType, Color> typeColors = new EnumMap<OsmType, Color>(OsmType.class);

	public OsmElementProperty() {
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

	public Color deriveColorFromType(OsmType osmType) {
		Color color = this.typeColors.get(OsmType.UNKNOWN);
		if (typeColors.get(osmType) != null) color = this.typeColors.get(osmType);
		return color;
	}
}
