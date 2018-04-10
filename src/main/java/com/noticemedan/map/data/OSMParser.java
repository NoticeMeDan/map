package com.noticemedan.map.data;

import com.noticemedan.map.App;
import com.noticemedan.map.data.io.OSMMapData;
import com.noticemedan.map.data.osm.OSMRootNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;

@Slf4j
public class OSMParser {
	@Getter
	private OSMRootNode rootNode;

	public OSMParser() {
		BufferedInputStream inputStream = new BufferedInputStream(App.class.getResourceAsStream("/bornholm.osm"));
		OSMMapData osmMapData = new OSMMapData();
		rootNode = osmMapData.deserialize(inputStream);
		log.info("# of Nodes: " + rootNode.getNodes().size());
	}
}
