package com.noticemedan.map.model;

import com.noticemedan.map.model.KDTree.Rect;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.Data;

import java.util.List;

@Data
public class MapObject implements Comparable<MapObject> {
	private OSMType osmType;
	private List<Point2D> points;
	private Color color;
	private boolean open; // TODO @Simon
	private Point2D avgPoint;
	private Rect bounds;
	private boolean depthEven = true; // Is this object at even depth in KD-Tree?

	@Override
	public int compareTo(MapObject that) {
		if(depthEven) {
			if (this.avgPoint.getX() > that.avgPoint.getX()) return 1;
			if (this.avgPoint.getX() == that.avgPoint.getX()) return 0;
			if (this.avgPoint.getX() < that.avgPoint.getX()) return -1;
		} else {
			if (this.avgPoint.getY() > that.avgPoint.getY()) return 1;
			if (this.avgPoint.getY() == that.avgPoint.getY()) return 0;
			if (this.avgPoint.getY() < that.avgPoint.getY()) return -1;
		}
		return 0;
	}

	public double getDiagonalSize() {
		double lengthX = bounds.getX2() - bounds.getX1();
		double lengthY = bounds.getY2() - bounds.getY1();
		return Math.sqrt(Math.pow(lengthX, 2) + Math.pow(lengthY, 2));
	}
}
