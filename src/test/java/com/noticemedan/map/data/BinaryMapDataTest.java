package com.noticemedan.map.data;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.kdtree.KdNode;
import com.noticemedan.map.model.utilities.Coordinate;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;

public class BinaryMapDataTest {

	private KdNode kdNode;
	private OsmElement[] osmElements;
	private OsmElement[] before;
	private OsmElement[] after;

	@BeforeTest
	void buildSmallKDTree() {
		osmElements = new OsmElement[8];
		for(int i = 0; i < 8; i++) {
			osmElements[i] = new OsmElement();
		}
		osmElements[0].setAvgPoint(new Coordinate(1,10));
		osmElements[1].setAvgPoint(new Coordinate(2,3));
		osmElements[2].setAvgPoint(new Coordinate(3,8));
		osmElements[3].setAvgPoint(new Coordinate(4,6));
		osmElements[4].setAvgPoint(new Coordinate(5,1));
		osmElements[5].setAvgPoint(new Coordinate(6,7));
		osmElements[6].setAvgPoint(new Coordinate(7,2));
		osmElements[7].setAvgPoint(new Coordinate(9,9));

		this.kdNode = new KdNode(osmElements, 5);


		this.kdNode = new KdNode(osmElements, 5);
		System.out.println("ID: " + this.kdNode.getBinaryID() + "\n");
	}

	@Test
	void serializeKDTree() {
		System.out.println("TEST 1 - SERIALIZE WORK");

		this.kdNode.elementsToBinary();
	}

	@Test(dependsOnMethods = "serializeKDTree")
	void deserialize() {
		System.out.println("TEST 2 - DESERIALIZE WORK");

		this.kdNode.setElements(this.kdNode.elementsFromBinary());
	}

	@Test(dependsOnMethods = "deserialize")
	void equalArrays() {
		System.out.println("TEST 3 - ARRAYS ARE EQUAL");
		this.before = this.kdNode.getElements();
		this.kdNode.setElements(this.kdNode.elementsFromBinary());
		this.after = this.kdNode.getElements();
		assertEquals(this.before,this.after);
	}

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
		File file = new File("./src/main/java/com/noticemedan/map/data/binarydatafiles/" + this.kdNode.getBinaryID() + ".dat");
		if(file.delete()) System.out.println("\nDELETED BINARY-TEST-FILE: " + file.getName());
	}
}
