package com.noticemedan.mappr.model.map;

import java.io.Serializable;

public enum Type implements Serializable {
	//ROADS (see wiki)
	MOTORWAY,
	PRIMARY,
	TRUNK,
	SECONDARY,
	TERTIARY,
	FOOTWAY,
	FOOTPATH,
	CYCLEWAY,
	TRACK,
	SERVICE,
	PATH,
	RESIDENTIAL,
	ROAD,
	UNCLASSIFIED,

	//Non-used roads
	RUNWAY,
	TAXIWAY,
	RAIL,

	//OTHER TYPES
	WATER,
	GRASSLAND,
	SAND,
	BUILDING,
	COASTLINE,
	TREE_ROW,
	HEATH,
	PLAYGROUND,
	GARDEN,
	PARK,
	FOREST,
	ADDRESS,
	AERODROME,
	UNKNOWN
}
