package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.osm.Element;
import com.noticemedan.map.model.osm.Element;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.Rect;
import com.noticemedan.map.model.utilities.Stopwatch;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static org.testng.Assert.*;

public class KDTreeTest {

	Element[] pointsOneElementKDTree;
	Element[] pointsForSmallKDTree;
	Element[] pointsForMultiMedianKDTree;

	KDTree multiMedianKDTree;
	KDTree oneElementKDTree;
	KDTree smallKDTree;

	KDTree randomBornHolmTree;

	@BeforeTest
	public void buildSmallKDTree() {
		pointsForSmallKDTree = new Element[8];
		pointsForSmallKDTree[0] = new Element();
		pointsForSmallKDTree[0].setAvgPoint(new Coordinate(1,10));
		pointsForSmallKDTree[1] = new Element();
		pointsForSmallKDTree[1].setAvgPoint(new Coordinate(2,3));
		pointsForSmallKDTree[2] = new Element();
		pointsForSmallKDTree[2].setAvgPoint(new Coordinate(3,8));
		pointsForSmallKDTree[3] = new Element();
		pointsForSmallKDTree[3].setAvgPoint(new Coordinate(4,6));
		pointsForSmallKDTree[4] = new Element();
		pointsForSmallKDTree[4].setAvgPoint(new Coordinate(5,1));
		pointsForSmallKDTree[5] = new Element();
		pointsForSmallKDTree[5].setAvgPoint(new Coordinate(6,7));
		pointsForSmallKDTree[6] = new Element();
		pointsForSmallKDTree[6].setAvgPoint(new Coordinate(7,2));
		pointsForSmallKDTree[7] = new Element();
		pointsForSmallKDTree[7].setAvgPoint(new Coordinate(9,9));

		smallKDTree = new KDTree(pointsForSmallKDTree, 2);
	}

	@Test
	public void testSmallKDTree_LeafNodeValues_Positive() {
		//Test some random leaf nodes values
		assertEquals(smallKDTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getElements()[0].getAvgPoint().getX(), 2.0);
		assertEquals(smallKDTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getElements()[0].getAvgPoint().getY(), 3.0);

		assertEquals(smallKDTree.getRootNode().getRightChild().getRightChild().getElements()[0].getAvgPoint().getX(), 9.0);
		assertEquals(smallKDTree.getRootNode().getRightChild().getRightChild().getElements()[0].getAvgPoint().getX(), 9.0);

		assertEquals(smallKDTree.getRootNode().getLeftChild().getRightChild().getElements()[1].getAvgPoint().getX(), 1.0);
		assertEquals(smallKDTree.getRootNode().getLeftChild().getRightChild().getElements()[1].getAvgPoint().getY(), 10.0);
	}

	@Test
	public void testSmallKDTree_NodeSplitValues_Positive() {
		//Test some random node split values
		assertEquals(smallKDTree.getRootNode().getSplitValue(), 5.0);
		assertEquals(smallKDTree.getRootNode().getLeftChild().getSplitValue(), 6.0);
	}

	@Test
	public void testSmallKDTree_LeafNodeValues_Negative() {
		//Test some random node split values
		assertNotEquals(smallKDTree.getRootNode().getSplitValue(), 4.5);
	}

	@Test
	public void testSmallKDTree_NodeSplitValues_Negative() {
		//Test some random node split values
		assertNotEquals(smallKDTree.getRootNode().getRightChild().getLeftChild().getElements()[1].getAvgPoint().getX(), 8);
		assertNotEquals(smallKDTree.getRootNode().getRightChild().getLeftChild().getElements()[1].getAvgPoint().getY(), 3);

	}

	public Element[] buildRandomPoints(int N) {
		Element[] randomGeneratedPoints = new Element[N];

		for(int i = 0; i < N; i++) {
			randomGeneratedPoints[i] = new Element();
			randomGeneratedPoints[i].setAvgPoint(new Coordinate(Math.random(), Math.random()));
		}
		return randomGeneratedPoints;
	}

	@Ignore @Test //Usually takes 25 seconds
	public void testVeryLargeKDTreeSpeed() { //10E7 OsmElements, endpoints 1000
		Stopwatch stopwatch = new Stopwatch();
		KDTree veryLargeKDTree = new KDTree(buildRandomPoints(10000000), 1000);
		System.out.println(stopwatch.elapsedTime()); //TODO convert to log
		assertTrue(stopwatch.elapsedTime() < 30);
	}

	@BeforeTest
	public void buildOneElementKDTree() {
		pointsOneElementKDTree = new Element[1];
		pointsOneElementKDTree[0] = new Element();
		pointsOneElementKDTree[0].setAvgPoint(new Coordinate(1,10));
		oneElementKDTree = new KDTree(pointsOneElementKDTree, 1);
	}

	@Test
	public void oneElementKDTree_Postive() {
		assertEquals(oneElementKDTree.getRootNode().getElements()[0].getAvgPoint().getX(),1.0);
		assertEquals(oneElementKDTree.getRootNode().getElements()[0].getAvgPoint().getY(),10.0);
	}

	//Delete test?
	@BeforeTest
	public void buildMultiMedianKDTree() {
		pointsForMultiMedianKDTree = new Element[5];
		pointsForMultiMedianKDTree[0] = new Element();
		pointsForMultiMedianKDTree[0].setAvgPoint(new Coordinate(1,8));
		pointsForMultiMedianKDTree[1] = new Element();
		pointsForMultiMedianKDTree[1].setAvgPoint(new Coordinate(5,3));
		pointsForMultiMedianKDTree[2] = new Element();
		pointsForMultiMedianKDTree[2].setAvgPoint(new Coordinate(5,8));
		pointsForMultiMedianKDTree[3] = new Element();
		pointsForMultiMedianKDTree[3].setAvgPoint(new Coordinate(5,6));
		pointsForMultiMedianKDTree[4] = new Element();
		pointsForMultiMedianKDTree[4].setAvgPoint(new Coordinate(9,8));
		multiMedianKDTree = new KDTree(pointsForMultiMedianKDTree, 1);
	}

	@Test
	public void rectCompletelyInRect_Positive_1() {
		Rect smallRect = new Rect(12,0,14,2);
		Rect largeRect = new Rect(7,-2,15,2);
		assertEquals(KDTree.rectCompletelyInRect(smallRect, largeRect), true);
	}

	@Test
	public void rectCompletelyInRect_Positive_2_EqualRects() {
		//If two rects are equal one is contained within the other
		Rect smallRect = new Rect(7,0,14,2);
		Rect largeRect = new Rect(7,0,14,2);
		assertEquals(KDTree.rectCompletelyInRect(smallRect, largeRect), true);
	}

	@Test
	public void rectCompletelyInRect_Positive_3_Infinity() {
		//With infinity
		Rect smallRect = new Rect(7,0,14,2);
		Rect largeRect = new Rect(Double.NEGATIVE_INFINITY,0,14,2);
		assertEquals(KDTree.rectCompletelyInRect(smallRect, largeRect), true);
	}

	@Test
	public void rectCompletelyInRect_Negative_1() {
		Rect smallRect = new Rect(7,-2,15,2);
		Rect largeRect = new Rect(7,0,14,2);
		assertEquals(KDTree.rectCompletelyInRect(smallRect, largeRect), false);
	}

	@Test
	public void rangeIntersectsRange_Positive_1() {
		assertEquals(KDTree.rangeIntersectsRange(1,3,2,4), true);
	}

	@Test
	public void rangeIntersectsRange_Positive_2_Infinity() {
		assertEquals(KDTree.rangeIntersectsRange(Double.NEGATIVE_INFINITY,3,2,Double.POSITIVE_INFINITY), true);
	}

	@Test
	public void rangeIntersectsRange_Negative_1() {
		assertEquals(KDTree.rangeIntersectsRange(2,3,4,6), false);
	}

	@Test
	public void pointInRect_Positive_1 () {
		Element point = new Element();
		point.setAvgPoint(new Coordinate(3,3));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_2_PointOnEdge () {
		Element point = new Element();
		point.setAvgPoint(new Coordinate(2,3));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Ignore @Test //TODO solve this test case in KDTree.pointInRect()
	public void pointInRect_Positive_3_PointOnEdge_NegativeCoord () {
		Element point = new Element();
		point.setAvgPoint(new Coordinate(-3,-3));
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Negative_1 () {
		Element point = new Element();
		point.setAvgPoint(new Coordinate(5,5));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}

	@Test
	public void pointInRect_Negative_2_NegativeCoord () {
		Element point = new Element();
		point.setAvgPoint(new Coordinate(-5,-5));
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_1() {
		Rect query = new Rect(0.5,7.5,4,10.5);
		List<Element> result = smallKDTree.rangeSearch(query);
		assertEquals(result.size(), 2);

		//Check a point
		assertEquals(result.get(0).getAvgPoint().getX(), 3.0);
		assertEquals(result.get(0).getAvgPoint().getY(), 8.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_2() {
		Rect query = new Rect(3.5,0.5,6.5,8.5);
		List<Element> result = smallKDTree.rangeSearch(query);
		assertEquals(result.size(),3);

		//Check a point
		assertEquals(result.get(2).getAvgPoint().getX(),6.0);
		assertEquals(result.get(2).getAvgPoint().getY(),7.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Negative_1() {
		Rect query = new Rect(8.0,4.0,10,6);
		List<Element> result = smallKDTree.rangeSearch(query);
		assertTrue(result.size() <= 0);
	}


	@Test
	public void build_randomBornHolmTree_rangesearchAll_1() {
		int N = 45000;
		Element[] randomGeneratedPoints = new Element[N];
		Random r = new Random();

		for(int i = 0; i < N; i++) {
			//min-max-min: Makes a random double in interval min-max.
			//double randomValueX = min + (max - min) * r.nextDouble();
			double randomValueX = 14 + (15 - 14) * r.nextDouble();
			double randomValueY = 54 + (55 - 54) * r.nextDouble();

			randomGeneratedPoints[i] = new Element();
			randomGeneratedPoints[i].setAvgPoint(new Coordinate(randomValueX, randomValueY));
		}
		//return randomGeneratedPoints;
		randomBornHolmTree = new KDTree(randomGeneratedPoints, 100);
		List results = randomBornHolmTree.rangeSearch(new Rect(14, 54, 15, 55));
		assertEquals(results.size(), 45000);
	}

	@Test
	public void build_randomBornHolmTree_rangesearchAll_2() {
		int N = 45000;
		Element[] randomGeneratedPoints = new Element[N];
		Random r = new Random();

		for(int i = 0; i < N; i++) {
			//min-max-min: Makes a random double in interval min-max.
			//double randomValueX = min + (max - min) * r.nextDouble();
			double randomValueX = 14.371 + (15.5370002 - 14.371) * r.nextDouble();
			double randomValueY = 54.883 + (55.383 - 54.883) * r.nextDouble();

			randomGeneratedPoints[i] = new Element();
			randomGeneratedPoints[i].setAvgPoint(new Coordinate(randomValueX, randomValueY));
		}
		//return randomGeneratedPoints;
		randomBornHolmTree = new KDTree(randomGeneratedPoints, 100);
		List results = randomBornHolmTree.rangeSearch(new Rect(14.3, 54.8, 15.6, 55.4));
		assertEquals(results.size(), 45000);
	}

	@Test
	public void build_randomBornHolmTree_rangesearchAll_3() {
		int N = 45000;
		Element[] randomGeneratedPoints = new Element[N];
		Random r = new Random();

		for(int i = 0; i < N; i++) {
			//min-max-min: Makes a random double in interval min-max.
			//double randomValueX = min + (max - min) * r.nextDouble();
			double randomValueX = 14.371 + (15.5370002 - 14.371) * r.nextDouble();
			double randomValueY = -54.883 + (-55.383 - -54.883) * r.nextDouble();

			randomGeneratedPoints[i] = new Element();
			randomGeneratedPoints[i].setAvgPoint(new Coordinate(randomValueX, randomValueY));
		}
		//return randomGeneratedPoints;
		randomBornHolmTree = new KDTree(randomGeneratedPoints, 100);
		List results = randomBornHolmTree.rangeSearch(new Rect(14.3, -55.4, 15.6, -54.8));

		assertEquals(results.size(), 45000);
	}

}
