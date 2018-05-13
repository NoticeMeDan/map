package com.noticemedan.mappr.model.kdtree;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.service.ForestService;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.collection.Vector;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.noticemedan.mappr.model.map.Type.*;
import static org.testng.AssertJUnit.assertEquals;

public class ForestServiceTest {

	private ForestService smallForest;
	private Vector<Element> ve = Vector.empty();
	private Vector<Element> cve = Vector.empty();

	@BeforeTest
	public void buildSmallKDTrees() {
		double[] x = new double[] {1, 2, 3, 4, 5, 6, 7, 9};
		double[] y = new double[] {10, 3, 8, 6, 1, 7, 2, 9};
		double[] x2 = new double[] {3, 1, 2, 9, 5, 6, 8, 4};
		double[] y2 = new double[] {12, 5, 8, 9, 2, 4, 3, 6};
		Type[] enums = new Type[] {ROAD, WATER, SECONDARY, MOTORWAY};
		Element el;
		Element coastlineE;


		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++ ) {
				el = new Element();
				el.setAvgPoint(new Coordinate(x[j], y[j]));
				el.setType(enums[i]);
				this.ve = this.ve.append(el);

				coastlineE = new Element();
				coastlineE.setAvgPoint(new Coordinate(x2[j], y2[j]));
				coastlineE.setType(COASTLINE);
				this.cve = this.cve.append(coastlineE);
			}
		}

		this.smallForest = new ForestService(ve, cve);
	}

	// Test some random rangeSearches with multiple tree forests.
	@Test
	public void rangeSearch_smallForest_zoomLevel0_Positive_1() {
		Rect query = new Rect(0.5,7.5,3.5, 10.5);
		Vector<Element> results = smallForest.rangeSearch(query, 0.4);
		assertEquals(results.size(), 2);
		assertEquals(results.get(0).getType(), MOTORWAY);
		assertEquals(results.get(0).getAvgPoint().getX(), 1.0);
		assertEquals(results.get(0).getAvgPoint().getY(), 10.0);
	}

	@Test
	public void rangeSearch_smallForest_zoomLevel1_Positive_2() {
		Rect query = new Rect(4.5,0.5,10, 10);
		Vector<Element> results = smallForest.rangeSearch(query, 0.6);
		assertEquals(results.size(), 4);
		assertEquals(results.get(results.size()-1).getType(), MOTORWAY);
		assertEquals(results.get(results.size()-1).getAvgPoint().getX(), 9.0);
		assertEquals(results.get(results.size()-1).getAvgPoint().getY(), 9.0);
	}

	@Test
	public void rangeSearch_smallForest_zoomLevel2_Positive_3() {
		Rect query = new Rect(0.5,5.5,9.5, 10.5);
		Vector<Element> results = smallForest.rangeSearch(query, 5);
		assertEquals(results.size(), 10);
		assertEquals(results.get(2).getType(), SECONDARY);
		assertEquals(results.get(2).getAvgPoint().getX(), 4.0);
		assertEquals(results.get(2).getAvgPoint().getY(), 6.0);
	}

	@Test
	public void rangeSearch_smallForest_zoomLevel3_Positive_3() {
		Rect query = new Rect(0.5,5.5,9.5, 10.5);
		Vector<Element> results = smallForest.rangeSearch(query, 15);
		assertEquals(results.size(), 20);
		assertEquals(results.get(2).getType(), ROAD);
		assertEquals(results.get(2).getAvgPoint().getX(), 4.0);
		assertEquals(results.get(2).getAvgPoint().getY(), 6.0);
	}

	@Test
	public void rangeSearch_smallForest_zoomLevel4_Positive_3() {
		Rect query = new Rect(4.5,0.5,10, 10);
		Vector<Element> results = smallForest.rangeSearch(query, 45);
		assertEquals(results.size(), 16);
		assertEquals(results.get(2).getType(), ROAD);
		assertEquals(results.get(2).getAvgPoint().getX(), 7.0);
		assertEquals(results.get(2).getAvgPoint().getY(), 2.0);
	}
}
