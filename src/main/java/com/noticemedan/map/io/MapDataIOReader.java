package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;

public interface MapDataIOReader {
	Osm deserialize(String path);
}
