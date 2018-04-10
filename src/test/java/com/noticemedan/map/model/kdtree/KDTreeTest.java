package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.utilities.Rect;
import com.noticemedan.map.model.utilities.Stopwatch;
import javafx.geometry.Point2D;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class KDTreeTest {

	OSMMaterialElement[] pointsOneElementKDTree;
	OSMMaterialElement[] pointsForSmallKDTree;
	OSMMaterialElement[] pointsForMultiMedianKDTree;

	KDTree multiMedianKDTree;
	KDTree oneElementKDTree;
	KDTree smallKDTree;

	@BeforeTest
	public void buildSmallKDTree() {
		pointsForSmallKDTree = new OSMMaterialElement[8];
		pointsForSmallKDTree[0] = new OSMMaterialElement();
		pointsForSmallKDTree[0].setAvgPoint(new Point2D(1,10));
		pointsForSmallKDTree[1] = new OSMMaterialElement();
		pointsForSmallKDTree[1].setAvgPoint(new Point2D(2,3));
		pointsForSmallKDTree[2] = new OSMMaterialElement();
		pointsForSmallKDTree[2].setAvgPoint(new Point2D(3,8));
		pointsForSmallKDTree[3] = new OSMMaterialElement();
		pointsForSmallKDTree[3].setAvgPoint(new Point2D(4,6));
		pointsForSmallKDTree[4] = new OSMMaterialElement();
		pointsForSmallKDTree[4].setAvgPoint(new Point2D(5,1));
		pointsForSmallKDTree[5] = new OSMMaterialElement();
		pointsForSmallKDTree[5].setAvgPoint(new Point2D(6,7));
		pointsForSmallKDTree[6] = new OSMMaterialElement();
		pointsForSmallKDTree[6].setAvgPoint(new Point2D(7,2));
		pointsForSmallKDTree[7] = new OSMMaterialElement();
		pointsForSmallKDTree[7].setAvgPoint(new Point2D(9,9));

		smallKDTree = new KDTree(pointsForSmallKDTree, 2);
	}

	@Test
	public void testSmallKDTree_LeafNodeValues_Positive() {
		//Test some random leaf nodes values
		assertEquals(smallKDTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getPoints()[0].getAvgPoint().getX(), 2.0);
		assertEquals(smallKDTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getPoints()[0].getAvgPoint().getY(), 3.0);

		assertEquals(smallKDTree.getRootNode().getRightChild().getRightChild().getPoints()[0].getAvgPoint().getX(), 9.0);
		assertEquals(smallKDTree.getRootNode().getRightChild().getRightChild().getPoints()[0].getAvgPoint().getX(), 9.0);

		assertEquals(smallKDTree.getRootNode().getLeftChild().getRightChild().getPoints()[1].getAvgPoint().getX(), 1.0);
		assertEquals(smallKDTree.getRootNode().getLeftChild().getRightChild().getPoints()[1].getAvgPoint().getY(), 10.0);
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
		assertNotEquals(smallKDTree.getRootNode().getRightChild().getLeftChild().getPoints()[1].getAvgPoint().getX(), 8);
		assertNotEquals(smallKDTree.getRootNode().getRightChild().getLeftChild().getPoints()[1].getAvgPoint().getY(), 3);

	}

	public OSMMaterialElement[] buildRandomPoints(int N) {
		OSMMaterialElement[] randomGeneratedPoints = new OSMMaterialElement[N];

		for(int i = 0; i < N; i++) {
			randomGeneratedPoints[i] = new OSMMaterialElement();
			randomGeneratedPoints[i].setAvgPoint(new Point2D(Math.random(), Math.random()));
		}
		return randomGeneratedPoints;
	}

	@Ignore @Test //Usually takes 25 seconds
	public void testVeryLargeKDTreeSpeed() { //10E7 points, endpoints 1000
		Stopwatch stopwatch = new Stopwatch();
		KDTree veryLargeKDTree = new KDTree(buildRandomPoints(10000000), 1000);
		System.out.println(stopwatch.elapsedTime()); //TODO convert to log
		assertTrue(stopwatch.elapsedTime() < 30);
	}

	@BeforeTest
	public void buildOneElementKDTree() {
		pointsOneElementKDTree = new OSMMaterialElement[1];
		pointsOneElementKDTree[0] = new OSMMaterialElement();
		pointsOneElementKDTree[0].setAvgPoint(new Point2D(1,10));
		oneElementKDTree = new KDTree(pointsOneElementKDTree, 1);
	}

	@Test
	public void oneElementKDTree_Postive() {
		assertEquals(oneElementKDTree.getRootNode().getPoints()[0].getAvgPoint().getX(),1.0);
		assertEquals(oneElementKDTree.getRootNode().getPoints()[0].getAvgPoint().getY(),10.0);
	}

	//Delete test?
	@BeforeTest
	public void buildMultiMedianKDTree() {
		pointsForMultiMedianKDTree = new OSMMaterialElement[5];
		pointsForMultiMedianKDTree[0] = new OSMMaterialElement();
		pointsForMultiMedianKDTree[0].setAvgPoint(new Point2D(1,8));
		pointsForMultiMedianKDTree[1] = new OSMMaterialElement();
		pointsForMultiMedianKDTree[1].setAvgPoint(new Point2D(5,3));
		pointsForMultiMedianKDTree[2] = new OSMMaterialElement();
		pointsForMultiMedianKDTree[2].setAvgPoint(new Point2D(5,8));
		pointsForMultiMedianKDTree[3] = new OSMMaterialElement();
		pointsForMultiMedianKDTree[3].setAvgPoint(new Point2D(5,6));
		pointsForMultiMedianKDTree[4] = new OSMMaterialElement();
		pointsForMultiMedianKDTree[4].setAvgPoint(new Point2D(9,8));
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
		OSMMaterialElement point = new OSMMaterialElement();
		point.setAvgPoint(new Point2D(3,3));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_2_PointOnEdge () {
		OSMMaterialElement point = new OSMMaterialElement();
		point.setAvgPoint(new Point2D(2,3));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_3_PointOnEdge_NegativeCoord () {
		OSMMaterialElement point = new OSMMaterialElement();
		point.setAvgPoint(new Point2D(-2,-3));
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Negative_1 () {
		OSMMaterialElement point = new OSMMaterialElement();
		point.setAvgPoint(new Point2D(5,5));
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}

	@Test
	public void pointInRect_Negative_2_NegativeCoord () {
		OSMMaterialElement point = new OSMMaterialElement();
		point.setAvgPoint(new Point2D(-5,-5));
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_1() {
		Rect query = new Rect(0.5,7.5,4,10.5);
		List<OSMMaterialElement> result = smallKDTree.rangeSearch(query);
		assertEquals(result.size(), 2);

		//Check a point
		assertEquals(result.get(0).getAvgPoint().getX(), 3.0);
		assertEquals(result.get(0).getAvgPoint().getY(), 8.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_2() {
		Rect query = new Rect(3.5,0.5,6.5,8.5);
		List<OSMMaterialElement> result = smallKDTree.rangeSearch(query);
		assertEquals(result.size(),3);

		//Check a point
		assertEquals(result.get(2).getAvgPoint().getX(),6.0);
		assertEquals(result.get(2).getAvgPoint().getY(),7.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Negative_1() {
		Rect query = new Rect(8.0,4.0,10,6);
		List<OSMMaterialElement> result = smallKDTree.rangeSearch(query);
		assertTrue(result.size() <= 0);
	}
}
