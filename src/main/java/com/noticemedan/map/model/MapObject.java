package com.noticemedan.map.model;

import javafx.geometry.Point2D;
import lombok.Data;

import java.util.List;

@Data
public class MapObject {
	private OSMType osmType;
	private List<Point2D> points;
}
