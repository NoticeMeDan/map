package com.noticemedan.map.model.utilities;

public class TextFormatter {
	static public String formatDistance(Double distance, int decimals) {
		if(decimals < 0) new RuntimeException("Number of decimals cannot be 0");
		String distanceAbbreviation = "km";
		if (distance < 1) distanceAbbreviation = "m";
		if (distance < 1) distance *= 1000;
		String format = "%." + decimals + "f";
		String withoutAbbreviation = String.format(format, distance);
		return withoutAbbreviation + " " + distanceAbbreviation;
	}
}