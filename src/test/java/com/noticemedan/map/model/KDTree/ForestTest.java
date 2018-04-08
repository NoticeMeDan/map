package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.OSMType;
import javafx.geometry.Point2D;
import static org.testng.Assert.*;
import org.testng.annotations.*;
import java.util.List;

public class ForestTest {

	MapObject[][] mapObjects_smallKDTrees;
	int[] maxElementsAtLeaf_smallKDTrees;
	Forest smallForest;

	@BeforeTest
	public void buildSmallKDTrees() {
		mapObjects_smallKDTrees = new MapObject[3][];
		maxElementsAtLeaf_smallKDTrees = new int[] {1,1,1};

		MapObject[] mapObjectsForSmallKDTree0 = new MapObject[8];
		mapObjectsForSmallKDTree0[0] = new MapObject();
		mapObjectsForSmallKDTree0[0].setAvgPoint(new Point2D(1,10));
		mapObjectsForSmallKDTree0[0].setOsmType(OSMType.ROAD);
		mapObjectsForSmallKDTree0[1] = new MapObject();
		mapObjectsForSmallKDTree0[1].setAvgPoint(new Point2D(2,3));
		mapObjectsForSmallKDTree0[1].setOsmType(OSMType.ROAD);
		mapObjectsForSmallKDTree0[2] = new MapObject();
		mapObjectsForSmallKDTree0[2].setAvgPoint(new Point2D(3,8));
		mapObjectsForSmallKDTree0[2].setOsmType(OSMType.ROAD);
		mapObjectsForSmallKDTree0[3] = new MapObject();
		mapObjectsForSmallKDTree0[3].setAvgPoint(new Point2D(4,6));
		mapObjectsForSmallKDTree0[3].setOsmType(OSMType.ROAD);
		mapObjectsForSmallKDTree0[4] = new MapObject();
		mapObjectsForSmallKDTree0[4].setAvgPoint(new Point2D(5,1));
		mapObjectsForSmallKDTree0[4].setOsmType(OSMType.ROAD);
		mapObjectsForSmallKDTree0[5] = new MapObject();
		mapObjectsForSmallKDTree0[5].setAvgPoint(new Point2D(6,7));
		mapObjectsForSmallKDTree0[5].setOsmType(OSMType.ROAD);
		mapObjectsForSmallKDTree0[6] = new MapObject();
		mapObjectsForSmallKDTree0[6].setAvgPoint(new Point2D(7,2));
		mapObjectsForSmallKDTree0[6].setOsmType(OSMType.ROAD);
		mapObjectsForSmallKDTree0[7] = new MapObject();
		mapObjectsForSmallKDTree0[7].setAvgPoint(new Point2D(9,9));
		mapObjectsForSmallKDTree0[7].setOsmType(OSMType.ROAD);
		mapObjects_smallKDTrees[0] = mapObjectsForSmallKDTree0;

		MapObject[] mapObjectsForSmallKDTree1 = new MapObject[8];
		mapObjectsForSmallKDTree1[0] = new MapObject();
		mapObjectsForSmallKDTree1[0].setAvgPoint(new Point2D(1,10));
		mapObjectsForSmallKDTree1[0].setOsmType(OSMType.WATER);
		mapObjectsForSmallKDTree1[1] = new MapObject();
		mapObjectsForSmallKDTree1[1].setAvgPoint(new Point2D(2,3));
		mapObjectsForSmallKDTree1[1].setOsmType(OSMType.WATER);
		mapObjectsForSmallKDTree1[2] = new MapObject();
		mapObjectsForSmallKDTree1[2].setAvgPoint(new Point2D(3,8));
		mapObjectsForSmallKDTree1[2].setOsmType(OSMType.WATER);
		mapObjectsForSmallKDTree1[3] = new MapObject();
		mapObjectsForSmallKDTree1[3].setAvgPoint(new Point2D(4,6));
		mapObjectsForSmallKDTree1[3].setOsmType(OSMType.WATER);
		mapObjectsForSmallKDTree1[4] = new MapObject();
		mapObjectsForSmallKDTree1[4].setAvgPoint(new Point2D(5,1));
		mapObjectsForSmallKDTree1[4].setOsmType(OSMType.WATER);
		mapObjectsForSmallKDTree1[5] = new MapObject();
		mapObjectsForSmallKDTree1[5].setAvgPoint(new Point2D(6,7));
		mapObjectsForSmallKDTree1[5].setOsmType(OSMType.WATER);
		mapObjectsForSmallKDTree1[6] = new MapObject();
		mapObjectsForSmallKDTree1[6].setAvgPoint(new Point2D(7,2));
		mapObjectsForSmallKDTree1[6].setOsmType(OSMType.WATER);
		mapObjectsForSmallKDTree1[7] = new MapObject();
		mapObjectsForSmallKDTree1[7].setAvgPoint(new Point2D(9,9));
		mapObjectsForSmallKDTree1[7].setOsmType(OSMType.WATER);
		mapObjects_smallKDTrees[1] = mapObjectsForSmallKDTree1;

		MapObject[] mapObjectsForSmallKDTree2 = new MapObject[8];
		mapObjectsForSmallKDTree2[0] = new MapObject();
		mapObjectsForSmallKDTree2[0].setAvgPoint(new Point2D(1,10));
		mapObjectsForSmallKDTree2[0].setOsmType(OSMType.TREE);
		mapObjectsForSmallKDTree2[1] = new MapObject();
		mapObjectsForSmallKDTree2[1].setAvgPoint(new Point2D(2,3));
		mapObjectsForSmallKDTree2[1].setOsmType(OSMType.TREE);
		mapObjectsForSmallKDTree2[2] = new MapObject();
		mapObjectsForSmallKDTree2[2].setAvgPoint(new Point2D(3,8));
		mapObjectsForSmallKDTree2[2].setOsmType(OSMType.TREE);
		mapObjectsForSmallKDTree2[3] = new MapObject();
		mapObjectsForSmallKDTree2[3].setAvgPoint(new Point2D(4,6));
		mapObjectsForSmallKDTree2[3].setOsmType(OSMType.TREE);
		mapObjectsForSmallKDTree2[4] = new MapObject();
		mapObjectsForSmallKDTree2[4].setAvgPoint(new Point2D(5,1));
		mapObjectsForSmallKDTree2[4].setOsmType(OSMType.TREE);
		mapObjectsForSmallKDTree2[5] = new MapObject();
		mapObjectsForSmallKDTree2[5].setAvgPoint(new Point2D(6,7));
		mapObjectsForSmallKDTree2[5].setOsmType(OSMType.TREE);
		mapObjectsForSmallKDTree2[6] = new MapObject();
		mapObjectsForSmallKDTree2[6].setAvgPoint(new Point2D(7,2));
		mapObjectsForSmallKDTree2[6].setOsmType(OSMType.TREE);
		mapObjectsForSmallKDTree2[7] = new MapObject();
		mapObjectsForSmallKDTree2[7].setAvgPoint(new Point2D(9,9));
		mapObjectsForSmallKDTree2[7].setOsmType(OSMType.TREE);
		mapObjects_smallKDTrees[2] = mapObjectsForSmallKDTree2;

		ForestCreator forestCreator = new ForestCreator(mapObjects_smallKDTrees, maxElementsAtLeaf_smallKDTrees);
		smallForest = forestCreator.getForest();
	}

	// Test some random rangeSearches with multiple tree forests.
	@Test
	public void rangeSearch_smallForest_zoomLevel0_Positive_1() {
		Rect query = new Rect(0.5,7.5,3.5, 10.5);
		List<MapObject> results = smallForest.rangeSearch(query, 0);
		assertEquals(results.size(), 2);
		assertEquals(results.get(0).getOsmType(), OSMType.ROAD);
		assertEquals(results.get(0).getAvgPoint().getX(), 3.0);
		assertEquals(results.get(0).getAvgPoint().getY(), 8.0);
	}

	@Test
	public void rangeSearch_smallForest_zoomLevel1_Positive_2() {
		Rect query = new Rect(4.5,0.5,10, 10);
		List<MapObject> results = smallForest.rangeSearch(query, 1);
		assertEquals(results.size(), 8);
		assertEquals(results.get(results.size()-1).getOsmType(), OSMType.WATER);
		assertEquals(results.get(results.size()-1).getAvgPoint().getX(), 9.0);
		assertEquals(results.get(results.size()-1).getAvgPoint().getY(), 9.0);
	}

	@Test
	public void rangeSearch_smallForest_zoomLevel2_Positive_3() {
		Rect query = new Rect(0.5,5.5,9.5, 10.5);
		List<MapObject> results = smallForest.rangeSearch(query, 2);
		assertEquals(results.size(), 15);
		assertEquals(results.get(2).getOsmType(), OSMType.ROAD);
		assertEquals(results.get(2).getAvgPoint().getX(), 1.0);
		assertEquals(results.get(2).getAvgPoint().getY(), 10.0);
	}
}
