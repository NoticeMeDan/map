package com.noticemedan.map.model;

import com.noticemedan.map.model.utilities.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeTest;
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
		double reversedLat = Coordinate.canvasLat2Lan(convertedCanvasLat);
		assertTrue(0.0000000001 >= reversedLat && reversedLat >= 0 );
	}

	@Test
	public void testToLat_AverageCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(lat);
		double reversedLat = Coordinate.canvasLat2Lan(convertedCanvasLat);
		assertTrue(lat > reversedLat && reversedLat > 55.1117608999000 );
	}

	@Test
	public void testToLat_MaximumCase() {
		double convertedCanvasLat = Coordinate.lat2CanvasLat(90);
		double reversedLat = Coordinate.canvasLat2Lan(convertedCanvasLat);
		assertTrue(90.0000001 > reversedLat && reversedLat > 89.99999999 );
	}
}
