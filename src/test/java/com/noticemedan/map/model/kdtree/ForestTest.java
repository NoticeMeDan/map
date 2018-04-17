//package com.noticemedan.map.model.KDTree;
//
//import com.noticemedan.map.model.OSMMaterialElement;
//import com.noticemedan.map.model.OSMType;
//import com.noticemedan.map.model.utilities.Rect;
//import javafx.geometry.Point2D;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import java.util.List;
//
//import static com.noticemedan.map.model.OSMType.*;
//import static org.testng.Assert.assertEquals;
//
//public class ForestTest {
//
//	OSMMaterialElement[][] mapObjects_smallKDTrees;
//	int[] maxElementsAtLeaf_smallKDTrees;
//	Forest smallForest;
//
//	@BeforeTest
//	public void buildSmallKDTrees() {
//		mapObjects_smallKDTrees = new OSMMaterialElement[3][];
//		maxElementsAtLeaf_smallKDTrees = new int[] {1,1,1};
//
//		double[] x = new double[] {1, 2, 3, 4, 5, 6, 7, 9};
//		double[] y = new double[] {10, 3, 8, 6, 1, 7, 2, 9};
//		OSMType[] enums = new OSMType[] {ROAD, WATER, TREE};
//
//		for (int i = 0; i < mapObjects_smallKDTrees.length; i++) {
//			OSMMaterialElement[] mapObjectsForSmallKDTree = new OSMMaterialElement[8];
//			for (int j = 0; j < mapObjectsForSmallKDTree.length; j++ ) {
//				mapObjectsForSmallKDTree[j] = new OSMMaterialElement();
//				mapObjectsForSmallKDTree[j].setAvgPoint(new Point2D(x[j], y[j]));
//				mapObjectsForSmallKDTree[j].setOsmType(enums[i]);
//			}
//			mapObjects_smallKDTrees[i] = mapObjectsForSmallKDTree;
//		}
//
//		ForestCreator forestCreator = new ForestCreator(mapObjects_smallKDTrees, maxElementsAtLeaf_smallKDTrees);
//		smallForest = forestCreator.getForest();
//	}
//
//	// Test some random rangeSearches with multiple tree forests.
//	@Test
//	public void rangeSearch_smallForest_zoomLevel0_Positive_1() {
//		Rect query = new Rect(0.5,7.5,3.5, 10.5);
//		List<OSMMaterialElement> results = smallForest.rangeSearch(query, 0);
//		assertEquals(results.size(), 2);
//		assertEquals(results.getListOfOSMMaterialElements(0).getOsmType(), ROAD);
//		assertEquals(results.getListOfOSMMaterialElements(0).getAvgPoint().getX(), 3.0);
//		assertEquals(results.getListOfOSMMaterialElements(0).getAvgPoint().getY(), 8.0);
//	}
//
//	@Test
//	public void rangeSearch_smallForest_zoomLevel1_Positive_2() {
//		Rect query = new Rect(4.5,0.5,10, 10);
//		List<OSMMaterialElement> results = smallForest.rangeSearch(query, 1);
//		assertEquals(results.size(), 8);
//		assertEquals(results.getListOfOSMMaterialElements(results.size()-1).getOsmType(), OSMType.WATER);
//		assertEquals(results.getListOfOSMMaterialElements(results.size()-1).getAvgPoint().getX(), 9.0);
//		assertEquals(results.getListOfOSMMaterialElements(results.size()-1).getAvgPoint().getY(), 9.0);
//	}
//
//	@Test
//	public void rangeSearch_smallForest_zoomLevel2_Positive_3() {
//		Rect query = new Rect(0.5,5.5,9.5, 10.5);
//		List<OSMMaterialElement> results = smallForest.rangeSearch(query, 2);
//		assertEquals(results.size(), 15);
//		assertEquals(results.getListOfOSMMaterialElements(2).getOsmType(), ROAD);
//		assertEquals(results.getListOfOSMMaterialElements(2).getAvgPoint().getX(), 1.0);
//		assertEquals(results.getListOfOSMMaterialElements(2).getAvgPoint().getY(), 10.0);
//	}
//}
