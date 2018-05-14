package com.noticemedan.mappr.model.util;

public class TextFormatter {
	public static String formatDistance(Double distance, int decimals) {
		if(decimals < 0) new RuntimeException("Number of decimals cannot be 0");
		String distanceAbbreviation = "m";
		if (distance > 1000) {
			distanceAbbreviation = "km";
			distance /= 1000;
		}
		String format = "%." + decimals + "f";
		String withoutAbbreviation = String.format(format, distance);
		return withoutAbbreviation + " " + distanceAbbreviation;
	}

	public static String formatTime(Double timeInHours) {
		int hours = (int) Math.floor(timeInHours);
		int minutes = (int) Math.floor((timeInHours - hours) * 60);

		return (hours == 0) ? minutes + "m" : hours + "t " + minutes + "m";
	}
}
