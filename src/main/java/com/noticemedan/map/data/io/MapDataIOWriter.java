package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.Osm;

import java.io.File;

public interface MapDataIOWriter {
	void serialize(File file, Osm rootNode);
}
