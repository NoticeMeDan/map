package com.noticemedan.map.data;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.kdtree.KDTreeNode;
import com.noticemedan.map.model.utilities.Coordinate;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

public class BinaryConverterTest {

	private KDTreeNode kdTreeNode;
	private OsmElement[] osmElements;

	@BeforeTest
	void buildSmallKDTree() {
		osmElements = new OsmElement[8];

		osmElements[0] = OsmElement.builder().avgPoint(new Coordinate(1,10)).build();
		osmElements[1] = OsmElement.builder().avgPoint(new Coordinate(2,3)).build();
		osmElements[2] = OsmElement.builder().avgPoint(new Coordinate(3,8)).build();
		osmElements[3] = OsmElement.builder().avgPoint(new Coordinate(4,6)).build();
		osmElements[4] = OsmElement.builder().avgPoint(new Coordinate(5,1)).build();
		osmElements[5] = OsmElement.builder().avgPoint(new Coordinate(6,7)).build();
		osmElements[6] = OsmElement.builder().avgPoint(new Coordinate(7,2)).build();
		osmElements[7] = OsmElement.builder().avgPoint(new Coordinate(9,9)).build();

		this.kdTreeNode = new KDTreeNode(osmElements, 5);
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
		this.kdTreeNode.setOsmElements(this.kdTreeNode.elementsFromBinary());
	}

	@Test
	void equalArrays() {
		System.out.println("TEST 3 - ARRAYS ARE EQUAL");
		OsmElement[] before = this.kdTreeNode.getOsmElements();
		this.kdTreeNode.elementsToBinary();
		this.kdTreeNode.setOsmElements(this.kdTreeNode.elementsFromBinary());
		assertEquals(before,this.kdTreeNode.getOsmElements());
	}

	@Test
	void equalElements() {
		System.out.println("TEST 4 - ELEMENTS ARE THE SAME");
		OsmElement[] before = this.kdTreeNode.getOsmElements();
		this.kdTreeNode.elementsToBinary();
		this.kdTreeNode.setOsmElements(this.kdTreeNode.elementsFromBinary());
		OsmElement[] after = this.kdTreeNode.getOsmElements();

		for(int i = 0; i < this.kdTreeNode.getOsmElements().length; i++) {
			assertEquals(after[i],before[i]);
		}
	}

	@AfterTest
	void clearFiles() {
		File file = new File("./binaryData/" + this.kdTreeNode.getBinaryID() + ".dat");
		if(file.delete()) System.out.println("\nDELETED BINARY-TEST-FILE: " + file.getName());
	}
}
