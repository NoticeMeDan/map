package com.noticemedan.map;

import com.noticemedan.map.data.io.XMLMapData;
import com.noticemedan.map.data.osm.Osm;

import java.io.File;

public class App {
    public static void main(String[] args) {
		XMLMapData xmlMapper = new XMLMapData();
		try {
			File file = new File(String.valueOf(App.class.getResource("/smaller.osm")).split(":")[1]);
			//File file = new File("/Users/elias/Projects/Code/School/map/denmark-latest.osm");
			file.exists();
			Osm rootNode = xmlMapper.deserialize(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
