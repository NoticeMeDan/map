package com.noticemedan.mappr.model.util;

import com.noticemedan.mappr.model.map.Element;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * A Rectangle class.
 */

@Data
@AllArgsConstructor
public class Rect implements Serializable {
	double x1;
	double y1;
	double x2;
	double y2;

	static public boolean rectCompletelyInRect(Rect smallRect, Rect largeRect) {
		boolean smallRectXRangeInLargeRectXRange = largeRect.getX1() <= smallRect.getX1() && smallRect.getX1() <= smallRect.getX2() && smallRect.getX2() <= largeRect.getX2();
		boolean smallRectYRangeInLargeRectYRange = largeRect.getY1() <= smallRect.getY1() && smallRect.getY1() <= smallRect.getY2() && smallRect.getY2() <= largeRect.getY2();
		return smallRectXRangeInLargeRectXRange && smallRectYRangeInLargeRectYRange;
	}

	static public boolean rectInRect(Rect smallRect, Rect largeRect) {
		boolean smallRectXRangeInLargeRectXRange = largeRect.getX1() <= smallRect.getX2() && smallRect.getX2() <= largeRect.getX2();
		boolean smallRectYRangeInLargeRectYRange = largeRect.getY1() <= smallRect.getY2() && smallRect.getY2() <= largeRect.getY2();
		return smallRectXRangeInLargeRectXRange && smallRectYRangeInLargeRectYRange;
	}

	static public boolean rangeIntersectsRange(double a, double b, double c, double d) {
		// Do range a-b and c-d intersect?
		return a <= d && b >= c;
	}

	static public boolean pointInRect(Element osmElement, Rect rect) {
		boolean part1 = rect.getX1() <= osmElement.getAvgPoint().getX();
		boolean part2 = osmElement.getAvgPoint().getX() <= rect.getX2();
		boolean part3 = rect.getY1() <= osmElement.getAvgPoint().getY();
		boolean part4 = osmElement.getAvgPoint().getY() <= rect.getY2();
		return part1 && part2 && part3 && part4;
	}
}
