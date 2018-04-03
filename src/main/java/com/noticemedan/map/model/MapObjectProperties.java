package com.noticemedan.map.model;

import com.noticemedan.map.data.osm.Tag;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class MapObjectProperties {
	HashMap<OSMType, Color> osmColors = new HashMap<>();

	public MapObjectProperties() {
		osmColors.put(OSMType.ROAD, Color.LIGHTGRAY);
		osmColors.put(OSMType.HIGHWAY, Color.ORANGE);
		osmColors.put(OSMType.BUILDING, Color.rgb(125,125,125));
		osmColors.put(OSMType.GRASSLAND, Color.rgb(12,158,89));
		osmColors.put(OSMType.WATER, Color.rgb(40,84,210));
		osmColors.put(OSMType.SAND, Color.YELLOW);
		osmColors.put(OSMType.TREE_ROW, Color.rgb(12,158,89));
		osmColors.put(OSMType.HEATH, Color.rgb(12,158,89));
		osmColors.put(OSMType.PLAYGROUND, Color.rgb(88,232,93));
		osmColors.put(OSMType.GARDEN, Color.rgb(14,232,93));
		osmColors.put(OSMType.PARK, Color.rgb(14,184,118));
	}

	public Color derriveColorFromOSMType(OSMType osmType) {
		Color color = Color.PINK; // UNKNOWN
		if (osmColors.get(osmType) != null)
			color = osmColors.get(color);
		return color;
	}

	public OSMType derriveOSMTypeFromTag(Tag t) {
		if (t.getK().equals("building"))
			return OSMType.BUILDING;
		if (t.getK().equals("natural")) {
			if (t.getV().equals("water"))
				return OSMType.WATER;
			if (t.getV().equals("tree"))
				return OSMType.TREE;
			if (t.getV().equals("grassland"))
				return OSMType.GRASSLAND;
			if (t.getV().equals("sand"))
				return OSMType.SAND;
			if (t.getV().equals("tree_row"))
				return OSMType.TREE_ROW;
			if (t.getV().equals("heath"))
				return OSMType.HEATH;
			if (t.getV().equals("coastline"))
				return OSMType.COASTLINE;
		}
		if (t.getK().equals("leisure")) {
			if (t.getV().equals("playground"))
				return OSMType.PLAYGROUND;
			if (t.getV().equals("garden"))
				return OSMType.GARDEN;
			if (t.getV().equals("park"))
				return OSMType.PARK;
		}
		if (t.getK().equals("highway")) {
			if (t.getV().equals("highway"))
				return OSMType.HIGHWAY;
			else
				return OSMType.ROAD;
		}
		return OSMType.UNKNOWN;
	}


}
