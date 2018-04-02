package com.noticemedan.map.model.KDTree;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.*;

public class KDTreeTest {

	KDTreePoint[] randomGeneratedPoints;
	KDTreePoint[] pointsOneElementKDTree;
	KDTreePoint[] pointsForSmallKDTRee;
	KDTreePoint[] pointsForMultiMedianKDTree;

	KDTree multiMedianKDTree;
	KDTree oneElementKDTree;
	KDTree smallKDTree;

	@BeforeTest
	public void buildSmallKDTree() {
		pointsForSmallKDTRee = new KDTreePoint[8];
		pointsForSmallKDTRee[0] = new KDTreePoint(1,10);
		pointsForSmallKDTRee[1] = new KDTreePoint(2,3);
		pointsForSmallKDTRee[2] = new KDTreePoint(3,8);
		pointsForSmallKDTRee[3] = new KDTreePoint(4,6);
		pointsForSmallKDTRee[4] = new KDTreePoint(5,1);
		pointsForSmallKDTRee[5] = new KDTreePoint(6,7);
		pointsForSmallKDTRee[6] = new KDTreePoint(7,2);
		pointsForSmallKDTRee[7] = new KDTreePoint(9,9);
		smallKDTree = new KDTree(pointsForSmallKDTRee, 2);
	}

	@Test
	public void testSmallKDTree_LeafNodeValues_Positive() {
		//Test some random leaf nodes values
		assertEquals(smallKDTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getPoints()[0].getX(), 2.0);
		assertEquals(smallKDTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getPoints()[0].getY(), 3.0);

		assertEquals(smallKDTree.getRootNode().getRightChild().getRightChild().getPoints()[0].getX(), 9.0);
		assertEquals(smallKDTree.getRootNode().getRightChild().getRightChild().getPoints()[0].getX(), 9.0);

		assertEquals(smallKDTree.getRootNode().getLeftChild().getRightChild().getPoints()[1].getX(), 1.0);
		assertEquals(smallKDTree.getRootNode().getLeftChild().getRightChild().getPoints()[1].getY(), 10.0);
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
		assertNotEquals(smallKDTree.getRootNode().getRightChild().getLeftChild().getPoints()[1].getX(), 8);
		assertNotEquals(smallKDTree.getRootNode().getRightChild().getLeftChild().getPoints()[1].getY(), 3);

	}

	public void buildRandomPoints() {
		int N = 10000000;
		randomGeneratedPoints = new KDTreePoint[N];

		for(int i = 0; i < N; i++) {
			randomGeneratedPoints[i] = new KDTreePoint(Math.random(), Math.random());
		}
	}

	@Ignore @Test
	public void testVeryLargeKDTreeSpeed() { //10E7 points
		buildRandomPoints();
		System.out.println("Started...");
		Stopwatch stopwatch = new Stopwatch();
		KDTree veryLargeKDTree = new KDTree(randomGeneratedPoints, 1000);
		assertTrue(stopwatch.elapsedTime() < 30);
		System.out.println(stopwatch.elapsedTime());
	}

	@BeforeTest
	public void buildOneElementKDTree() {
		pointsOneElementKDTree = new KDTreePoint[1];
		pointsOneElementKDTree[0] = new KDTreePoint(1,10);
		oneElementKDTree = new KDTree(pointsOneElementKDTree, 1);
	}

	@Test
	public void oneElementKDTree_Postive() {
		assertEquals(oneElementKDTree.getRootNode().getPoints()[0].getX(),1.0);
		assertEquals(oneElementKDTree.getRootNode().getPoints()[0].getY(),10.0);
	}

	//Delete test?
	@BeforeTest
	public void buildMultiMedianKDTree() {
		pointsForMultiMedianKDTree = new KDTreePoint[5];
		pointsForMultiMedianKDTree[0] = new KDTreePoint(1,8);
		pointsForMultiMedianKDTree[1] = new KDTreePoint(5,3);
		pointsForMultiMedianKDTree[2] = new KDTreePoint(5,8);
		pointsForMultiMedianKDTree[3] = new KDTreePoint(5,6);
		pointsForMultiMedianKDTree[4] = new KDTreePoint(9,8);
		multiMedianKDTree = new KDTree(pointsForMultiMedianKDTree, 1);
	}

	//Delete test?
	@Test
	public void testReportSubtree() {
		//smallKDTree.reportSubtree(smallKDTree.getRootNode());
		//ArrayList<KDTreePoint> queryResult = smallKDTree.rangeSearch();
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
		KDTreePoint point = new KDTreePoint(3,3);
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_2_PointOnEdge () {
		KDTreePoint point = new KDTreePoint(2,3);
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_3_PointOnEdge_NegativeCoord () {
		KDTreePoint point = new KDTreePoint(-2,-3);
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Negative_1 () {
		KDTreePoint point = new KDTreePoint(5,5);
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}

	@Test
	public void pointInRect_Negative_2_NegativeCoord () {
		KDTreePoint point = new KDTreePoint(-5,-5);
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}
}
