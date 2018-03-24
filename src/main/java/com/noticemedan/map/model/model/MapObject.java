package com.noticemedan.map.model.model;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MapObject implements MapObjectInterface {
	@Getter @Setter private OSMType osmType;
	@Getter @Setter private List<Point2D> points;

	@Override
	public OSMType getOSMType() {
		return osmType;
	}

	@Override
	public List<Point2D> getPoints() {
		return points;
	}
}
