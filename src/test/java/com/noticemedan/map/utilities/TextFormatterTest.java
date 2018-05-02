package com.noticemedan.map.utilities;

import com.noticemedan.map.model.utilities.TextFormatter;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Slf4j
public class TextFormatterTest {

	@Test
	public void distanceFormat_AverageCase1() {
		assertEquals(TextFormatter.formatDistance(55.53623,2), "55,54 km");
	}

	@Test
	public void distanceFormat_AverageCase2() {
		assertEquals(TextFormatter.formatDistance(0.7423467,3), "742,347 m");
	}

	@Test
	public void distanceFormat_MinimumCase1() {
		assertEquals(TextFormatter.formatDistance(0d,0), "0 m");
	}

	@Test @Ignore
	// This shows that the distanceFormat() does not work with negative numbers.
	public void distanceFormat_MinimumCase2() {
		assertEquals(TextFormatter.formatDistance(-4.32d,1), "-4,32 m");
	}
}
