package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.map.Type;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Line2D;

@Data
public class PathEdge {
	private double weight;
	private double speedLimit;
	private Type roadType;
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

	public Shape toShape() {
		double x1 = this.v.getLon();
		double y1 = this.v.getLat();
		double x2 = this.w.getLon();
		double y2 = this.w.getLat();
		return new Line2D.Double(x1,y1,x2,y2);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--\n");
		sb.append("v: {").append(v.getLon()).append(", ").append(v.getLat()).append("}\n");
		sb.append("w: {").append(w.getLon()).append(", ").append(w.getLat()).append("}\n");
		sb.append("weight: ").append(this.weight).append("\n");
		sb.append("--\n");
		return sb.toString();
	}
}
