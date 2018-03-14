package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;

public interface MapDataIOWriter {
	void serialize(String path, Osm rootNode);
}
