package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

public class MapDaoTest {
	private MapData original;
	private MapData test;
	private Path mapPath = Paths.get(MapDaoTest.class.getResource("/osm").getPath(), "test.map");

	@BeforeTest
	void prepare() throws Exception {
		this.original = new OsmDao().read(Paths.get(MapDaoTest.class.getResource("/osm/small.osm.zip").toURI()));
		new MapDao().write(this.mapPath, this.original);
		this.test = new MapDao().read(this.mapPath);
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
		Files.delete(this.mapPath);
	}
}
