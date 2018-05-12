package com.noticemedan.mappr.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Calendar;
import java.util.Date;

@Data
public class MapInfo {
	String name;
	Date date = new Date();
	double size;

	public MapInfo(String name, double size) {
		this.name = name;
		this.size = size;
	}
}
