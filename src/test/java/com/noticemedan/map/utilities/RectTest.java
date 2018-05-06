package com.noticemedan.map.utilities;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.Rect;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RectTest {
	@Test
	public void rectCompletelyInRect_Positive_1() {
		Rect smallRect = new Rect(12,0,14,2);
		Rect largeRect = new Rect(7,-2,15,2);
		assertEquals(Rect.rectCompletelyInRect(smallRect, largeRect), true);
	}

	@Test
	public void rectCompletelyInRect_Positive_2_EqualRects() {
		//If two rects are equal one is contained within the other
		Rect smallRect = new Rect(7,0,14,2);
		Rect largeRect = new Rect(7,0,14,2);
		assertEquals(Rect.rectCompletelyInRect(smallRect, largeRect), true);
	}

	@Test
	public void rectCompletelyInRect_Positive_3_Infinity() {
		//With infinity
		Rect smallRect = new Rect(7,0,14,2);
		Rect largeRect = new Rect(Double.NEGATIVE_INFINITY,0,14,2);
		assertEquals(Rect.rectCompletelyInRect(smallRect, largeRect), true);
	}

	@Test
	public void rectCompletelyInRect_Negative_1() {
		Rect smallRect = new Rect(7,-2,15,2);
		Rect largeRect = new Rect(7,0,14,2);
		assertEquals(Rect.rectCompletelyInRect(smallRect, largeRect), false);
	}

	@Test
	public void rectCompletelyInRect_Positive_4() {
		Rect smallRect = new Rect(-3, -3, -2, -2);
		Rect largeRect = new Rect(-4,-4,-1,-1);
		assertEquals(Rect.rectCompletelyInRect(smallRect, largeRect), true);
	}

	@Test
	public void rangeIntersectsRange_Positive_1() {
		assertEquals(Rect.rangeIntersectsRange(1,3,2,4), true);
	}

	@Test
	public void rangeIntersectsRange_Positive_2_Infinity() {
		assertEquals(Rect.rangeIntersectsRange(Double.NEGATIVE_INFINITY,3,2,Double.POSITIVE_INFINITY), true);
	}

	@Test
	public void rangeIntersectsRange_Negative_1() {
		assertEquals(Rect.rangeIntersectsRange(2,3,4,6), false);
	}

	@Test
	public void pointInRect_Positive_1 () {
		OsmElement point = new OsmElement();
		point.setAvgPoint(new Coordinate(3,3));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(Rect.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_2_PointOnEdge () {
		OsmElement point = new OsmElement();
		point.setAvgPoint(new Coordinate(2,3));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(Rect.pointInRect(point, rect), true);
	}

	@Ignore
	@Test //TODO solve this test case in KdTree.pointInRect()
	public void pointInRect_Positive_3_PointOnEdge_NegativeCoord () {
		OsmElement point = new OsmElement();
		point.setAvgPoint(new Coordinate(-3,-3));
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(Rect.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Negative_1 () {
		OsmElement point = new OsmElement();
		point.setAvgPoint(new Coordinate(5,5));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(Rect.pointInRect(point, rect), false);
	}

	@Test
	public void pointInRect_Negative_2_NegativeCoord () {
		OsmElement point = new OsmElement();
		point.setAvgPoint(new Coordinate(-5,-5));
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(Rect.pointInRect(point, rect), false);
	}
}
