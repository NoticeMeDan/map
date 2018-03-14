package com.noticemedan.map;

import com.noticemedan.map.io.XMLStrategyMapData;
import com.noticemedan.map.osm.Osm;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
		XMLStrategyMapData xmlMapper = new XMLStrategyMapData();
		try {
			String filename = App.class.getResource("/smaller.osm").toString();
			Osm rootNode = xmlMapper.deserialize(filename);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
