package com.noticemedan.mappr.model.util;

import com.noticemedan.mappr.view.util.TextFormatter;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@Slf4j
public class TextFormatterTest {
	@Test
	public void distanceFormat_AverageCase1() {
		assertEquals(TextFormatter.formatDistance(55536.23,2), "55.54 km");
	}

	@Test
	public void distanceFormat_AverageCase2() {
		assertEquals(TextFormatter.formatDistance(742.3467,3), "742.347 m");
	}

	@Test
	public void distanceFormat_MinimumCase1() {
		assertEquals(TextFormatter.formatDistance(0d,0), "0 m");
	}

	@Test
	// This shows that the distanceFormat() does not work with negative numbers.
	public void distanceFormat_MinimumCase2() {
		assertNotEquals(TextFormatter.formatDistance(-4.32d,1), "-4,32 m");
	}
}
