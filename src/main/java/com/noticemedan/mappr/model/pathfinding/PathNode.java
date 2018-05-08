package com.noticemedan.mappr.model.pathfinding;

import com.noticemedan.mappr.model.map.Type;
import io.vavr.collection.Vector;
import lombok.Builder;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Ellipse2D;

@Builder
@Data
public class PathNode {
	private int id;
	private double lon;
	private double lat;
	private int maxspeed;
	private Type roadType;
	Vector<PathEdge> edges;

	public int degree() {
		return edges.size();
	}

	public Shape toShape() {
		double size = 0.00005;
		return new Ellipse2D.Double(this.lon - (size/2), this.lat- (size/2), size,size);
	}

	public String toString() {
		return "[id: " + id + " :: {" + lon + ", " + lat + "}]";
	}
}
