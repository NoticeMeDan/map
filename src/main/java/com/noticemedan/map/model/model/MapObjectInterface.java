package com.noticemedan.map.model.model;

import javafx.geometry.Point2D;

import java.util.List;

public interface MapObjectInterface {
	OSMType getOSMType();
	List<Point2D> getPoints();
}
