package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.kdtree.KDTreeNode;
import com.noticemedan.mappr.model.util.Coordinate;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

public class MapDaoTest {

	MapData original;
	MapData test;

	@BeforeTest
	void prepare() throws Exception {
		this.original = new OsmDao().read(Paths.get("/home/elias/Projects/Code/map/src/main/resources/fyn.osm.zip"));
		this.test = new MapDao().write(Paths.get("test.map"), this.original);
	}

	@Test
	void testAddresses() throws Exception {
		assertEquals(this.original.getAddresses(), this.test.getAddresses());
	}

	@Test
	void testCoastlines() throws Exception {
		assertEquals(this.original.getCoastlineElements(), this.test.getCoastlineElements());
	}

	@Test
	void testElements() throws Exception {
		assertEquals(this.original.getElements(), this.test.getElements());
	}

	@AfterTest
	void clearFiles() throws Exception {
		Files.delete(Paths.get("test.map"));
	}
}
