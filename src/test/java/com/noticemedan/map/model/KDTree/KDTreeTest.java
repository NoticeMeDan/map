package com.noticemedan.map.model.KDTree;

import static org.testng.Assert.*;

import io.vavr.collection.Array;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class KDTreeTest {

	KDTreePoint[] randomGeneratedPoints;
	KDTreePoint[] pointsForSmallKDTRee;

	@BeforeTest
	public void buildRandomPoints() {
		int N = 1000;
		randomGeneratedPoints = new KDTreePoint[N];

		System.out.println("Begin making points");
		for(int i = 0; i < N; i++) {
			randomGeneratedPoints[i] = new KDTreePoint(Math.random(), Math.random());
		}
		System.out.println("Points ended, begin building kd tree");
	}

	@BeforeTest
	public void buildPointsForSmallKDTree() {
		pointsForSmallKDTRee = new KDTreePoint[8];
		pointsForSmallKDTRee[0] = new KDTreePoint(1,10);
		pointsForSmallKDTRee[1] = new KDTreePoint(2,3);
		pointsForSmallKDTRee[2] = new KDTreePoint(3,8);
		pointsForSmallKDTRee[3] = new KDTreePoint(4,6);
		pointsForSmallKDTRee[4] = new KDTreePoint(5,1);
		pointsForSmallKDTRee[5] = new KDTreePoint(6,7);
		pointsForSmallKDTRee[6] = new KDTreePoint(7,2);
		pointsForSmallKDTRee[7] = new KDTreePoint(9,9);
	}

	@Test
	public void testSmallKDTreeValidity() {
		KDTree smallKDTree = new KDTree(pointsForSmallKDTRee, 2);
	}

	
}
