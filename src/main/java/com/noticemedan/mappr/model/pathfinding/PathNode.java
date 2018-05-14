package com.noticemedan.mappr.model.pathfinding;

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
	Vector<PathEdge> edges;

	public int degree() {
		return edges.size();
	}

	public Shape toShape() {
		float size = 0.0002f;
		return new Ellipse2D.Float((float) this.lon - (size/2), (float) this.lat- (size/2), size,size);
	}

	public String toString() {
		return "[id: " + id + " :: {" + lon + ", " + lat + "}]";
	}
}
