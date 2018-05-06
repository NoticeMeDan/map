package com.noticemedan.map.model;

import com.noticemedan.map.model.osm.Amenity;
import com.noticemedan.map.model.osm.OsmType;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.Rect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class OsmElement implements Comparable<OsmElement>, Serializable {
	private OsmType osmType;
	private Amenity amenity;
	private Shape shape;
	private Color color;
	private boolean open; // TODO @Simon
	private Coordinate avgPoint;
	private Rect bounds;
	private boolean depthEven = true; // Is this object at even depth in KD-Tree?

	@Override
	public int compareTo(OsmElement that) {
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
}
