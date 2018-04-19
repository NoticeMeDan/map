package com.noticemedan.map.model.pathfinding;

import com.noticemedan.map.model.osm.OSMType;
import lombok.Data;

@Data
public class PathEdge {
	private double weight;
	private double speedLimit;
	private OSMType roadType;
	private PathNode v;
	private PathNode w;

	public PathEdge(PathNode v, PathNode w) {
		this.v = v;
		this.w = w;
		this.weight = computeWeight(v, w);
	}

	private double computeWeight(PathNode v, PathNode w) {
		double deltaLon = w.getLon() - v.getLon();
		double deltaLat = w.getLat() - v.getLat();
		return Math.sqrt(Math.pow(deltaLon, 2) + Math.pow(deltaLat, 2));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("v: {" + v.getLon() + ", " + v.getLat() + "}\n");
		sb.append("w: {" + w.getLon() + ", " + w.getLat() + "}\n");
		sb.append("weight: " + this.weight + "\n");
		sb.append("--\n");
		return sb.toString();
	}
}
