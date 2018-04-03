package com.noticemedan.map.model.KDTree;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.*;

import java.util.ArrayList;

public class KDTreeTest {

	KDMapObject[] pointsOneElementKDTree;
	KDMapObject[] pointsForSmallKDTRee;
	KDMapObject[] pointsForMultiMedianKDTree;

	KDTree multiMedianKDTree;
	KDTree oneElementKDTree;
	KDTree smallKDTree;
	KDTree largeKDTree_100000;

	@BeforeTest
	public void buildSmallKDTree() {
		pointsForSmallKDTRee = new KDMapObject[8];
		pointsForSmallKDTRee[0] = new KDMapObject(1,10);
		pointsForSmallKDTRee[1] = new KDMapObject(2,3);
		pointsForSmallKDTRee[2] = new KDMapObject(3,8);
		pointsForSmallKDTRee[3] = new KDMapObject(4,6);
		pointsForSmallKDTRee[4] = new KDMapObject(5,1);
		pointsForSmallKDTRee[5] = new KDMapObject(6,7);
		pointsForSmallKDTRee[6] = new KDMapObject(7,2);
		pointsForSmallKDTRee[7] = new KDMapObject(9,9);
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

	public KDMapObject[] buildRandomPoints(int N) {
		KDMapObject[] randomGeneratedPoints = new KDMapObject[N];

		for(int i = 0; i < N; i++) {
			randomGeneratedPoints[i] = new KDMapObject(Math.random(), Math.random());
		}
		return randomGeneratedPoints;
	}

	@Ignore @Test //Usually takes 25 seconds
	public void testVeryLargeKDTreeSpeed() { //10E7 points, endpoints 1000
		Stopwatch stopwatch = new Stopwatch();
		KDTree veryLargeKDTree = new KDTree(buildRandomPoints(10000000), 1000);
		assertTrue(stopwatch.elapsedTime() < 30);
	}

	@BeforeTest
	public void buildOneElementKDTree() {
		pointsOneElementKDTree = new KDMapObject[1];
		pointsOneElementKDTree[0] = new KDMapObject(1,10);
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
		pointsForMultiMedianKDTree = new KDMapObject[5];
		pointsForMultiMedianKDTree[0] = new KDMapObject(1,8);
		pointsForMultiMedianKDTree[1] = new KDMapObject(5,3);
		pointsForMultiMedianKDTree[2] = new KDMapObject(5,8);
		pointsForMultiMedianKDTree[3] = new KDMapObject(5,6);
		pointsForMultiMedianKDTree[4] = new KDMapObject(9,8);
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
		KDMapObject point = new KDMapObject(3,3);
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_2_PointOnEdge () {
		KDMapObject point = new KDMapObject(2,3);
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Positive_3_PointOnEdge_NegativeCoord () {
		KDMapObject point = new KDMapObject(-2,-3);
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), true);
	}

	@Test
	public void pointInRect_Negative_1 () {
		KDMapObject point = new KDMapObject(5,5);
		Rect rect = new Rect(2,2,4,4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}

	@Test
	public void pointInRect_Negative_2_NegativeCoord () {
		KDMapObject point = new KDMapObject(-5,-5);
		Rect rect = new Rect(-2,-2,-4,-4);
		assertEquals(KDTree.pointInRect(point, rect), false);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_1() {
		Rect query = new Rect(0.5,7.5,4,10.5);
		ArrayList<KDMapObject> result = smallKDTree.rangeSearch(query);
		assertEquals(result.size(), 2);

		//Check a point
		assertEquals(result.get(0).getX(), 3.0);
		assertEquals(result.get(0).getY(), 8.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_2() {
		Rect query = new Rect(3.5,0.5,6.5,8.5);
		ArrayList<KDMapObject> result = smallKDTree.rangeSearch(query);
		assertEquals(result.size(),3);

		//Check a point
		assertEquals(result.get(2).getX(),6.0);
		assertEquals(result.get(2).getY(),7.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Negative_1() {
		Rect query = new Rect(8.0,4.0,10,6);
		ArrayList<KDMapObject> result = smallKDTree.rangeSearch(query);
		assertTrue(result.size() <= 0);
	}
}
