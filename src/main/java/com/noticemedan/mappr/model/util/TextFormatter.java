package com.noticemedan.mappr.model.util;

import java.util.Locale;

public class TextFormatter {
	static public String formatDistance(Double distance, int decimals) {
		if(decimals < 0) new RuntimeException("Number of decimals cannot be 0");
		String distanceAbbreviation = "km";
		if (distance < 1) {
			distanceAbbreviation = "m";
			distance *= 1000;
		}
		String format = "%." + decimals + "f";
		String withoutAbbreviation = String.format(Locale.ROOT, format, distance);
		return withoutAbbreviation + " " + distanceAbbreviation;
	}
}
