package com.noticemedan.map.model.Utilities;

import javafx.geometry.Point2D;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rect {
	double x1;
	double y1;
	double x2;
	double y2;

	public Point2D getAveragePoint() {
		double avgX = (x2 + x1) / 2;
		double avgY = (y2 + y1) / 2;
		return new Point2D(avgX, avgY);
	}
}
