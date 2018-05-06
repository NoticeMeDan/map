package com.noticemedan.mappr.model.util;

import lombok.Data;

import java.awt.geom.Point2D;
import java.io.Serializable;

@Data
public class Coordinate extends Point2D implements Serializable {
	private double x;
	private double y;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
