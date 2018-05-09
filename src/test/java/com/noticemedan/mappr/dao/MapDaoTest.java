package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.kdtree.KDTreeNode;
import com.noticemedan.mappr.model.util.Coordinate;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

public class MapDaoTest {

	private MapData original;
	private MapData testData;

	@BeforeTest
	void before() throws IOException {
		this.original = new OsmDao().read(Paths.get("/Users/elias/Projects/Code/School/map/src/main/resources/fyn.osm.zip"));
		new MapDao().write(Paths.get("test.map"), this.original);
		this.testData = new MapDao().read(Paths.get("test.map"));
	}

	@Test
	void testElements() throws Exception {
		assertEquals(original.getElements(), testData.getElements());
	}

	@Test
	void testCoastlines() throws Exception {
		assertEquals(original.getCoastlineElements(), testData.getCoastlineElements());
	}

	@Test
	void testAddresses() throws Exception {
		assert(original.getAddresses(), testData.getAddresses());
	}

	@AfterTest
	void cleanup() throws IOException {
		Files.delete(Paths.get("test.map"));
	}
}
