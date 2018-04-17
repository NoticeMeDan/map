package com.noticemedan.map.model.pathfinding;

import com.noticemedan.map.model.osm.OSMType;

public class PathEdge {
	private double weight;
	private double speedLimit;
	private OSMType roadType;
	private PathNode v;
	private PathNode w;
}
