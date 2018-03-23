package com.noticemedan.map.model;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

public class DemoObject implements MapObjectInterface {

	@Getter @Setter OSMType OSMType;
	@Getter @Setter List<Point2D> points;

	public DemoObject(){
		this.OSMType = OSMType.UNKNOWN;
		this.points = new LinkedList<>();
	}
}
