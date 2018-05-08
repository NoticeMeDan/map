package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.kdtree.KDTreeNode;
import com.noticemedan.mappr.model.util.Coordinate;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;

import static org.testng.Assert.assertEquals;

public class MapDaoTest {

	private KDTreeNode kdTreeNode;
	private Element[] elements;
	private Element[] before;
	private Element[] after;

	@BeforeTest
	void buildSmallKDTree() {
		elements = new Element[8];
		for(int i = 0; i < 8; i++) {
			elements[i] = new Element();
		}
		elements[0].setAvgPoint(new Coordinate(1,10));
		elements[1].setAvgPoint(new Coordinate(2,3));
		elements[2].setAvgPoint(new Coordinate(3,8));
		elements[3].setAvgPoint(new Coordinate(4,6));
		elements[4].setAvgPoint(new Coordinate(5,1));
		elements[5].setAvgPoint(new Coordinate(6,7));
		elements[6].setAvgPoint(new Coordinate(7,2));
		elements[7].setAvgPoint(new Coordinate(9,9));

		this.kdTreeNode = new KDTreeNode(elements, 5);


		this.kdTreeNode = new KDTreeNode(elements, 5);
		System.out.println("ID: " + this.kdTreeNode.getBinaryID() + "\n");
	}

//	@Test
//	void serializeKDTree() {
//		System.out.println("TEST 1 - SERIALIZE WORK");
//
//		this.kdTreeNode.elementsToBinary();
//	}
//
//	@Test(dependsOnMethods = "serializeKDTree")
//	void deserialize() {
//		System.out.println("TEST 2 - DESERIALIZE WORK");
//
//		this.kdTreeNode.setElements(this.kdTreeNode.elementsFromBinary());
//	}
//
//	@Test(dependsOnMethods = "deserialize")
//	void equalArrays() {
//		System.out.println("TEST 3 - ARRAYS ARE EQUAL");
//		this.before = this.kdTreeNode.getElements();
//		this.kdTreeNode.setElements(this.kdTreeNode.elementsFromBinary());
//		this.after = this.kdTreeNode.getElements();
//		assertEquals(this.before,this.after);
//	}

	// Binary file handling will be done differently, so the following test is irrelevant.
/*	@Test(dependsOnMethods = "equalArrays")
	void equalElements() {
		System.out.println("TEST 4 - ELEMENTS ARE THE SAME");

		for(int i = 0; i < this.after.length; i++) {
			assertEquals(this.after[i], this.before[i]);
		}
	}*/

	@AfterTest
	void clearFiles() {
		File file = new File("./src/main/java/com/noticemedan/map/dao/binarydatafiles/" + this.kdTreeNode.getBinaryID() + ".dat");
		if(file.delete()) System.out.println("\nDELETED BINARY-TEST-FILE: " + file.getName());
	}
}
