package com.noticemedan.mappr.model.map;

import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class Element implements Comparable<Element>, Serializable {
	private Type type;
	private String name;
	private Shape shape;
	private int maxspeed;
	private Color color;
	private Coordinate avgPoint;
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

	public static Element cloneElement(Element other) {
		Element element = new Element();
		element.setType(other.getType());
		element.setName(other.getName());
		element.setShape(other.getShape());
		element.setMaxspeed(other.getMaxspeed());
		element.setColor(other.getColor());
		element.setAvgPoint(other.getAvgPoint());
		element.setDepthEven(other.isDepthEven());
		return element;
	}

	public boolean isRoad() {
		return type == Type.MOTORWAY ||
				type == Type.PRIMARY ||
				type == Type.TRUNK ||
				type == Type.SECONDARY ||
				type == Type.TERTIARY ||
				type == Type.FOOTWAY ||
				type == Type.FOOTPATH ||
				type == Type.CYCLEWAY ||
				type == Type.TRACK ||
				type == Type.SERVICE ||
				type == Type.PATH ||
				type == Type.RESIDENTIAL ||
				type == Type.ROAD ||
				type == Type.UNCLASSIFIED;
	}

	public boolean isDrivable() {
		return type == Type.MOTORWAY ||
				type == Type.PRIMARY ||
				type == Type.TRUNK ||
				type == Type.SECONDARY ||
				type == Type.TERTIARY ||
				type == Type.SERVICE ||
				type == Type.RESIDENTIAL ||
				type == Type.ROAD ||
				type == Type.UNCLASSIFIED;
	}

	public boolean isCyclable() {
		return type == Type.SECONDARY ||
				type == Type.TERTIARY ||
				type == Type.CYCLEWAY ||
				type == Type.TRACK ||
				type == Type.SERVICE ||
				type == Type.PATH ||
				type == Type.RESIDENTIAL ||
				type == Type.ROAD ||
				type == Type.UNCLASSIFIED;
	}

	public boolean isWalkable() {
		return type == Type.SECONDARY ||
				type == Type.TERTIARY ||
				type == Type.FOOTWAY ||
				type == Type.FOOTPATH ||
				type == Type.CYCLEWAY ||
				type == Type.TRACK ||
				type == Type.SERVICE ||
				type == Type.PATH ||
				type == Type.RESIDENTIAL ||
				type == Type.ROAD ||
				type == Type.UNCLASSIFIED;
	}
}
