package com.noticemedan.map.data;

import com.noticemedan.map.data.io.OsmMapData;
import com.noticemedan.map.data.osm.Osm;
import com.noticemedan.map.App;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;

@Slf4j
public class OsmParser {
	@Getter private Osm rootNode;

	public OsmParser() {
		BufferedInputStream inputStream = new BufferedInputStream(App.class.getResourceAsStream("/small.osm"));
		OsmMapData osmMapData = new OsmMapData();
		rootNode = osmMapData.deserialize(inputStream);
		log.info("# of Nodes: " + rootNode.getNodes().size());
	}
}
