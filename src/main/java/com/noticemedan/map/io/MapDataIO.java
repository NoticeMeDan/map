package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;

interface MapDataIO {
	Osm deserialize(String path);
	void serialize(String path, Osm rootNode);
}
