package com.noticemedan.mappr.model.kdtree;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import com.noticemedan.mappr.model.util.Stopwatch;
import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.*;

@Slf4j
public class KDTreeTest {

	Element[] pointsOneElementKDTree;
	Element[] pointsForSmallKDTree;
	Element[] pointsForMultiMedianKDTree;

	KdTree multiMedianKdTree;
	KdTree oneElementKdTree;
	KdTree smallKdTree;

	KdTree randomBornHolmTree;

	/* For white box testing: */
	Element element1;
	Element element2;
	Element element3;
	Element element4;
	Element element5;

	Element[] elementsA;
	Element[] elementsB;
	Element[] elementsC;
	Element[] elementsD;
	Element[] elementsE;
	Element[] elementsF;

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

		smallKdTree = new KdTree(pointsForSmallKDTree, 2);
	}

	@Test
	public void testSmallKDTree_LeafNodeValues_Positive() {
		//Test some random leaf nodes values
		assertEquals(smallKdTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getElements()[0].getAvgPoint().getX(), 2.0);
		assertEquals(smallKdTree.getRootNode().getLeftChild().getLeftChild().getLeftChild().getElements()[0].getAvgPoint().getY(), 3.0);

		assertEquals(smallKdTree.getRootNode().getRightChild().getRightChild().getElements()[0].getAvgPoint().getX(), 9.0);
		assertEquals(smallKdTree.getRootNode().getRightChild().getRightChild().getElements()[0].getAvgPoint().getX(), 9.0);

		assertEquals(smallKdTree.getRootNode().getLeftChild().getRightChild().getElements()[1].getAvgPoint().getX(), 1.0);
		assertEquals(smallKdTree.getRootNode().getLeftChild().getRightChild().getElements()[1].getAvgPoint().getY(), 10.0);
	}

	@Test
	public void testSmallKDTree_NodeSplitValues_Positive() {
		//Test some random node split values
		assertEquals(smallKdTree.getRootNode().getSplitValue(), 5.0);
		assertEquals(smallKdTree.getRootNode().getLeftChild().getSplitValue(), 6.0);
	}

	@Test
	public void testSmallKDTree_LeafNodeValues_Negative() {
		//Test some random node split values
		assertNotEquals(smallKdTree.getRootNode().getSplitValue(), 4.5);
	}

	@Test
	public void testSmallKDTree_NodeSplitValues_Negative() {
		//Test some random node split values
		assertNotEquals(smallKdTree.getRootNode().getRightChild().getLeftChild().getElements()[1].getAvgPoint().getX(), 8);
		assertNotEquals(smallKdTree.getRootNode().getRightChild().getLeftChild().getElements()[1].getAvgPoint().getY(), 3);

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
		KdTree veryLargeKdTree = new KdTree(buildRandomPoints(27000000), 1000);
		log.info(stopwatch.toString());
		assertTrue(stopwatch.elapsedTime() < 30);
	}

	@BeforeTest
	public void buildOneElementKDTree() {
		pointsOneElementKDTree = new Element[1];
		pointsOneElementKDTree[0] = new Element();
		pointsOneElementKDTree[0].setAvgPoint(new Coordinate(1,10));
		oneElementKdTree = new KdTree(pointsOneElementKDTree, 1);
	}

	@Test
	public void oneElementKDTree_Postive() {
		assertEquals(oneElementKdTree.getRootNode().getElements()[0].getAvgPoint().getX(),1.0);
		assertEquals(oneElementKdTree.getRootNode().getElements()[0].getAvgPoint().getY(),10.0);
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
		multiMedianKdTree = new KdTree(pointsForMultiMedianKDTree, 1);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_1() {
		Rect query = new Rect(0.5,7.5,4,10.5);
		Vector<Element> result = smallKdTree.rangeSearch(query);
		assertEquals(result.size(), 2);

		//Check a point
		assertEquals(result.get(0).getAvgPoint().getX(), 3.0);
		assertEquals(result.get(0).getAvgPoint().getY(), 8.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Positive_2() {
		Rect query = new Rect(3.5,0.5,6.5,8.5);
		Vector<Element> result = smallKdTree.rangeSearch(query);
		assertEquals(result.size(),3);

		//Check a point
		assertEquals(result.get(2).getAvgPoint().getX(),6.0);
		assertEquals(result.get(2).getAvgPoint().getY(),7.0);
	}

	@Test
	public void rangeSearch_SmallKDTree_Negative_1() {
		Rect query = new Rect(8.0,4.0,10,6);
		Vector<Element> result = smallKdTree.rangeSearch(query);
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
		randomBornHolmTree = new KdTree(randomGeneratedPoints, 100);
		Vector results = randomBornHolmTree.rangeSearch(new Rect(14, 54, 15, 55));
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
		randomBornHolmTree = new KdTree(randomGeneratedPoints, 100);
		Vector results = randomBornHolmTree.rangeSearch(new Rect(14.3, 54.8, 15.6, 55.4));
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
		randomBornHolmTree = new KdTree(randomGeneratedPoints, 100);
		Vector results = randomBornHolmTree.rangeSearch(new Rect(14.3, -55.4, 15.6, -54.8));

		assertEquals(results.size(), 45000);
	}

	@Test
	public void nearestNeighbor_AverageCase1() {
		Coordinate searchCoordinate = new Coordinate(3.1,8.1);
		Element nearestNeighbor = smallKdTree.nearestNeighbor(searchCoordinate);
		assertEquals(3.0, nearestNeighbor.getAvgPoint().getX());
		assertEquals(8.0, nearestNeighbor.getAvgPoint().getY());
	}

	@Test
	public void nearestNeighbor_AverageCase2() {
		Coordinate searchCoordinate = new Coordinate(5,4);
		Element nearestNeighbor = smallKdTree.nearestNeighbor(searchCoordinate);
		assertEquals(4.0, nearestNeighbor.getAvgPoint().getX());
		assertEquals(6.0, nearestNeighbor.getAvgPoint().getY());
	}

	@Test
	public void nearestNeighbor_AverageCase3() {
		Coordinate searchCoordinate = new Coordinate(-2,-2);
		Element nearestNeighbor = smallKdTree.nearestNeighbor(searchCoordinate);
		assertEquals(2.0, nearestNeighbor.getAvgPoint().getX());
		assertEquals(3.0, nearestNeighbor.getAvgPoint().getY());
	}

	/* To run these test set modifier to public static for splitArrayTestDatasetA() */
	/* @BeforeTest
	public void splitArrayDataSets() {
		// Create elements
		element1 = new Element();
		element1.setAvgPoint(new Coordinate(1,1));

		element2 = new Element();
		element2.setAvgPoint(new Coordinate(2,6));

		element3 = new Element();
		element3.setAvgPoint(new Coordinate(8,7));

		element4 = new Element();
		element4.setAvgPoint(new Coordinate(3,4));

		element5 = new Element();
		element5.setAvgPoint(new Coordinate(9,3));

		elementsA = new Element[0];

		//Insert elements into array
		elementsB = new Element[1];
		elementsB[0] = element1;

		elementsC = new Element[2];
		elementsC[0] = element1;
		elementsC[1] = element2;

		elementsD = new Element[3];
		elementsD[0] = element1;
		elementsD[1] = element2;
		elementsD[2] = element3;

		elementsE = new Element[4];
		elementsE[0] = element1;
		elementsE[1] = element2;
		elementsE[2] = element3;
		elementsE[3] = element4;

		elementsF = new Element[5];
		elementsF[0] = element1;
		elementsF[1] = element2;
		elementsF[2] = element3;
		elementsF[3] = element4;
		elementsF[4] = element5;
	}

	@Test(expectedExceptions = RuntimeException.class, groups = {"splitArrayWhitebox"})
	public void splitArrayTestDatasetA() {
		KdTree.splitPointArrayByMedian(elementsA);
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void splitArrayTestDatasetB() {
		KdTree.splitPointArrayByMedian(elementsB);
	}

	@Test
	public void splitArrayTestDatasetC() {
		Tuple2<Element[], Element[]> splitElement = KdTree.splitPointArrayByMedian(elementsC);
		assertEquals(splitElement._1.length, 1);
		assertEquals(splitElement._2.length, 1);
		assertEquals(splitElement._1[0].getAvgPoint().getX(), 1.0);
		assertEquals(splitElement._2[0].getAvgPoint().getX(), 2.0);
	}

	@Test
	public void splitArrayTestDatasetD() {
		Tuple2<Element[], Element[]> splitElement = KdTree.splitPointArrayByMedian(elementsD);
		assertEquals(splitElement._1.length, 2);
		assertEquals(splitElement._2.length, 1);
		assertEquals(splitElement._1[0].getAvgPoint().getX(), 1.0);
		assertEquals(splitElement._1[1].getAvgPoint().getX(), 2.0);
		assertEquals(splitElement._2[0].getAvgPoint().getX(), 8.0);
	}

	@Test
	public void splitArrayTestDatasetE() {
		Tuple2<Element[], Element[]> splitElement = KdTree.splitPointArrayByMedian(elementsE);
		assertEquals(splitElement._1.length, 3);
		assertEquals(splitElement._2.length, 1);
		assertEquals(splitElement._1[0].getAvgPoint().getX(), 1.0);
		assertEquals(splitElement._1[1].getAvgPoint().getX(), 2.0);
		assertEquals(splitElement._1[2].getAvgPoint().getX(), 3.0);
		assertEquals(splitElement._2[0].getAvgPoint().getX(), 8.0);
	}

	@Test
	public void splitArrayTestDatasetF() {
		Tuple2<Element[], Element[]> splitElement = KdTree.splitPointArrayByMedian(elementsF);
		assertEquals(splitElement._1.length, 3);
		assertEquals(splitElement._2.length, 2);
		assertEquals(splitElement._1[0].getAvgPoint().getX(), 1.0);
		assertEquals(splitElement._1[1].getAvgPoint().getX(), 2.0);
		assertEquals(splitElement._1[2].getAvgPoint().getX(), 3.0);
		assertEquals(splitElement._2[0].getAvgPoint().getX(), 8.0);
		assertEquals(splitElement._2[1].getAvgPoint().getX(), 9.0);
	}*/
}
