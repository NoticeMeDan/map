package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.util.Coordinate;
import io.vavr.collection.Set;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Line2D;

@Data
public class PathEdge {
	private double weight;
	private double speedLimit;
	private Set<TravelType> travelTypesAllowed;
	private PathNode v;
	private PathNode w;

	PathEdge(PathNode v, PathNode w) {
		this.v = v;
		this.w = w;
		this.weight = computeWeight(v, w);
		//System.out.println(v.getMaxspeed());
		this.speedLimit = v.getMaxspeed();
	}

	private double computeWeight(PathNode v, PathNode w) {
		double x1 = v.getLon();
		double x2 = w.getLon();
		double y1 = Coordinate.canvasLatToLat(v.getLat());
		double y2 = Coordinate.canvasLatToLat(w.getLat());
		Coordinate a = new Coordinate(x1,y1);
		Coordinate b = new Coordinate(x2,y2);
		return Coordinate.haversineDistance(a,b,6371000);
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
