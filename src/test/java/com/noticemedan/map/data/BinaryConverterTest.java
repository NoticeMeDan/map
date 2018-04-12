package com.noticemedan.map.data;

import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.kdtree.KDTreeNode;
import com.noticemedan.map.model.utilities.Coordinate;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

public class BinaryConverterTest {

	private KDTreeNode kdTreeNode;
	private OSMMaterialElement[] osmMaterialElements;

	@BeforeTest
	void buildSmallKDTree() {
		osmMaterialElements = new OSMMaterialElement[8];
		for(int i = 0; i < 8; i++) {
			osmMaterialElements[i] = new OSMMaterialElement();
		}
		osmMaterialElements[0].setAvgPoint(new Coordinate(1,10));
		osmMaterialElements[1].setAvgPoint(new Coordinate(2,3));
		osmMaterialElements[2].setAvgPoint(new Coordinate(3,8));
		osmMaterialElements[3].setAvgPoint(new Coordinate(4,6));
		osmMaterialElements[4].setAvgPoint(new Coordinate(5,1));
		osmMaterialElements[5].setAvgPoint(new Coordinate(6,7));
		osmMaterialElements[6].setAvgPoint(new Coordinate(7,2));
		osmMaterialElements[7].setAvgPoint(new Coordinate(9,9));

		this.kdTreeNode = new KDTreeNode(osmMaterialElements, 5);
		System.out.println("ID: " + this.kdTreeNode.getBinaryID() + "\n");
	}

	@Test
	void serializeKDTree() {
		System.out.println("TEST 1 - SERIALIZE WORK");
		this.kdTreeNode.elementsToBinary();
	}

	@Test
	void deserialize() {
		System.out.println("TEST 2 - DESERIALIZE WORK");
		this.kdTreeNode.elementsToBinary();
		this.kdTreeNode.setOsmMaterialElements(this.kdTreeNode.elementsFromBinary());
	}

	@Test
	void equalArrays() {
		System.out.println("TEST 3 - ARRAYS ARE EQUAL");
		OSMMaterialElement[] before = this.kdTreeNode.getOsmMaterialElements();
		this.kdTreeNode.elementsToBinary();
		this.kdTreeNode.setOsmMaterialElements(this.kdTreeNode.elementsFromBinary());
		assertEquals(before,this.kdTreeNode.getOsmMaterialElements());
	}

	@Test
	void equalElements() {
		System.out.println("TEST 4 - ELEMENTS ARE THE SAME");
		OSMMaterialElement[] before = this.kdTreeNode.getOsmMaterialElements();
		this.kdTreeNode.elementsToBinary();
		this.kdTreeNode.setOsmMaterialElements(this.kdTreeNode.elementsFromBinary());
		OSMMaterialElement[] after = this.kdTreeNode.getOsmMaterialElements();

		for(int i = 0; i < this.kdTreeNode.getOsmMaterialElements().length; i++) {
			assertEquals(after[i],before[i]);
		}
	}

	@AfterTest
	void clearFiles() {
		File file = new File("./binaryData/" + this.kdTreeNode.getBinaryID() + ".dat");
		if(file.delete()) System.out.println("\nDELETED BINARY-TEST-FILE: " + file.getName());
	}
}
