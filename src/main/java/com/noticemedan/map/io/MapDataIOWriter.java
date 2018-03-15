package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;

import java.io.File;

public interface MapDataIOWriter {
	void serialize(File file, Osm rootNode);
}
