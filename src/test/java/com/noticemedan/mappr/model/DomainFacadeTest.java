package com.noticemedan.mappr.model;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.pathfinding.TravelType;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class DomainFacadeTest {

	DomainFacade domain;
	Rect testArea;
	double zoom;
	Coordinate queryPoint;
	Coordinate queryPoint2;
	TravelType travelType;

	@BeforeTest
	public void init() {
		this.domain = new DomainFacade();
		this.testArea = new Rect(7,-2,15,2);
		this.queryPoint = new Coordinate(12.539723, 55.674252);
		this.queryPoint2 = new Coordinate(12.405602, 55.729174);
		this.travelType = TravelType.ALL;
		this.zoom = 0;
	}

	@Test
	public void nearestNeighBor() {
		Element e = this.domain.doNearestNeighborSearch(queryPoint, zoom);
		assertEquals(e.getMaxspeed(), 80);
		assertEquals(e.getType(), Type.WATER);
	}
}
