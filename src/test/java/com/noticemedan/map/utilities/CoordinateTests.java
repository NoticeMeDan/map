package com.noticemedan.map.utilities;

import com.noticemedan.map.model.utilities.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Slf4j
public class CoordinateTests {
	double lat = 55.1117609;

	/**
	 * Through these tests we want high precision for conversion between coordinate
	 * types.
	 */
	@Test
	public void testToCanvasLat_MinimumCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(0);
		assertTrue(convertedCanvasLat > 0 && convertedCanvasLat < 0.00000000001);
		//convertedCanvasLat will not return 0 because of double precision error.
	}

	@Test
	public void testToCanvasLat_AverageCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(lat);
		assertTrue(-66.32788969 > convertedCanvasLat && convertedCanvasLat > -66.32788971 );
	}

	@Test
	public void testToCanvasLat_MaximumCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(90);
		assertTrue(-2138.957801262 > convertedCanvasLat && convertedCanvasLat > -2138.957801264 );
	}

	@Test
	@Ignore //Returns -0.0!
	public void testToLat_MinimumCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(0);
		double reversedLat = Coordinate.canvasLat2Lat(convertedCanvasLat);
		assertTrue(0.0000000001 >= reversedLat && reversedLat >= 0 );
	}

	@Test
	public void testToLat_AverageCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(lat);
		double reversedLat = Coordinate.canvasLat2Lat(convertedCanvasLat);
		assertTrue(lat > reversedLat && reversedLat > 55.1117608999000 );
	}

	@Test
	public void testToLat_MaximumCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(90);
		double reversedLat = Coordinate.canvasLat2Lat(convertedCanvasLat);
		assertTrue(90.0000001 > reversedLat && reversedLat > 89.99999999 );
	}

	@Test
	public void testHaversineDistance_MinimumCase() {
		Coordinate a = new Coordinate(0, 0);
		Coordinate b = new Coordinate(0, 0);
		double distance = Coordinate.haversineDistance(a, b, 6371);
		assertTrue(distance == 0);
	}

	@Test
	public void testHaversineDistance_AverageCase() {
		Coordinate a = new Coordinate(12.539723, 55.674252);
		Coordinate b = new Coordinate(12.405602, 55.729174);
		double distance = Coordinate.haversineDistance(a, b, 6371);
		assertTrue(10.3885 > distance && distance > 10.3883);
	}

	@Test
	public void testHaversineDistance_MaximumCase() {
		Coordinate a = new Coordinate(-90, -90);
		Coordinate b = new Coordinate(90, 90);
		double distance = Coordinate.haversineDistance(a, b, 6371);
		assertTrue(20015.09 > distance && distance > 20015.07);
	}

	@Test
	public void testEuclidianDistance_MinimumCase() {
		Coordinate a = new Coordinate(0, 0);
		Coordinate b = new Coordinate(0, 0);
		double distance = Coordinate.euclidianDistance(a, b);
		assertTrue(distance == 0);
	}

	@Test
	public void testEuclidianDistance_AverageCase() {
		Coordinate a = new Coordinate(12.539723, 55.674252);
		Coordinate b = new Coordinate(12.405602, 55.729174);
		double distance = Coordinate.euclidianDistance(a, b);
		assertTrue(0.144931 > distance && distance > 0.144930);
	}

	@Test
	public void testEuclidianDistance_MaximumCase() {
		Coordinate a = new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		Coordinate b = new Coordinate(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		double distance = Coordinate.euclidianDistance(a, b);
		assertTrue(distance == Double.POSITIVE_INFINITY);
	}

	@Test
	public void testClosest_AverageCase_1() {
		Coordinate a = new Coordinate(10, 10);
		Coordinate b = new Coordinate(9, 9);
		Coordinate query = new Coordinate(1,1);
		assertTrue(b.equals(Coordinate.closest(query, a, b)));
	}

	@Test
	public void testClosest_AverageCase_2() {
		Coordinate a = new Coordinate(10, 10);
		Coordinate b = new Coordinate(-2, 3);
		Coordinate query = new Coordinate(0,0);
		assertTrue(b.equals(Coordinate.closest(query, a, b)));
	}
}
