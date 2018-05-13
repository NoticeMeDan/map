package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import com.noticemedan.mappr.model.map.Boundaries;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static org.testng.Assert.*;


public class OsmDaoTest {
	OsmDao osmDao = new OsmDao();
	MapData expected = MapData.builder()
			.boundaries(Boundaries.builder()
					.minLat(-67.29853505595685)
					.minLon(12.573)
					.maxLat(-67.329212443815)
					.maxLon(12.6031)
					.build())
			.elements(Vector.empty())
			.coastlineElements(Vector.empty())
			.addresses(Vector.empty())
			.poi(List.empty())
			.build();

	@Test
	public void getMapDataFromOsm() throws Exception {
		MapData test = this.osmDao.read(Paths.get(OsmDaoTest.class.getResource("/osm/testOsm.osm").toURI()));
		assertEquals(expected, test);
	}

	@Test(dependsOnMethods = "getMapDataFromOsm")
	public void getMapDataFromZip() throws Exception {
		MapData test = this.osmDao.read(Paths.get(OsmDaoTest.class.getResource("/osm/testOsm.osm.zip").toURI()));
		assertEquals(expected, test);
	}
}
