package com.noticemedan.map;

import com.noticemedan.map.io.XMLMapData;
import com.noticemedan.map.osm.Osm;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
		XMLMapData xmlMapper = new XMLMapData();
		try {
			File file = new File("/Users/elias/Projects/Code/School/map/target/classes/smaller.osm");
			Osm rootNode = xmlMapper.deserialize(file);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
