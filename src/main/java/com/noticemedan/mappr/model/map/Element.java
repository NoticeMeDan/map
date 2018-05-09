package com.noticemedan.mappr.model.map;

import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Element implements Comparable<Element>, Serializable {
	private Type type;
	private Path2D shape;
	private String name;
	private Color color;
	private boolean open; // TODO @Simon
	private Coordinate avgPoint;
	private Rect bounds;
	private boolean depthEven = true; // Is this object at even depth in KD-Tree?

	@Override
	public int compareTo(Element that) {
		if (depthEven) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Element element = (Element) o;
		return open == element.open &&
				depthEven == element.depthEven &&
				type == element.type &&
				Objects.equals(shape, element.shape) &&
				Objects.equals(color, element.color) &&
				Objects.equals(avgPoint, element.avgPoint) &&
				Objects.equals(bounds, element.bounds);
	}
}
