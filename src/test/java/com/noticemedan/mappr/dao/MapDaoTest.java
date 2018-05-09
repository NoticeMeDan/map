package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

public class MapDaoTest {
	private MapData original;
	private MapData test;

	@BeforeTest
	void prepare() throws Exception {
		this.original = new OsmDao().read(Paths.get("/home/elias/Projects/Code/map/src/main/resources/fyn.osm.zip"));
		new MapDao().write(Paths.get("test.map"), this.original);
		this.test = new MapDao().read(Paths.get("test.map"));
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
