package com.noticemedan.mappr.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Rect implements Serializable {
	double x1;
	double y1;
	double x2;
	double y2;

	public Coordinate getAveragePoint() {
		double avgX = (x2 + x1) / 2.0;
		double avgY = (y2 + y1) / 2.0;
		return new Coordinate(avgX, avgY);
	}
}
