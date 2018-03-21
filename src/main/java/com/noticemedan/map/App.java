package com.noticemedan.map;

import com.noticemedan.map.data.io.XMLMapData;
import com.noticemedan.map.data.osm.Osm;

import java.io.File;

public class App {
    public static void main(String[] args) {
		XMLMapData xmlMapper = new XMLMapData();
		File file = new File(String.valueOf(App.class.getResource("/smaller.osm")).split(":")[1]);
		//File file = new File("/home/elias/Projects/Code/map/denmark-latest.os");
		Osm rootNode = xmlMapper.deserialize(file);
		System.out.println("# of Nodes: " + rootNode.getNodes().size());
	}
}
