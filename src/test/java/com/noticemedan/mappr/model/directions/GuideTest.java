package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.model.pathfinding.PathEdge;
import com.noticemedan.mappr.model.service.ShortestPathService;
import io.vavr.collection.Vector;
import org.testng.annotations.Test;

public class GuideTest {

	private final DomainFacade domain = new DomainFacade();
	private Guide guide = new Guide();
	private Vector<PathEdge> route = domain.getRandomSP();

	@Test
	void testDirections() {
		for(String s : guide.getDirections(route)) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println("Distance: " + this.guide.getTraveleddistance());
	}


}
