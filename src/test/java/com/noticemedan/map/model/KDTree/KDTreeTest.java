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

	@Test
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
	public void testOneElementKDTree_Postive() {
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
}
