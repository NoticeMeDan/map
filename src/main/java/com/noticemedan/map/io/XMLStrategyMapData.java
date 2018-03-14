package com.noticemedan.map.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.noticemedan.map.osm.Osm;

public class XMLStrategyMapData implements MapDataIO {
	@Override
	public Osm deserialize(String path) {
		ObjectMapper mapper = new XmlMapper();
		Osm rootNode = mapper.
		return null;
	}

	@Override
	public void serialize(String path, Osm rootNode) {
	}
}
