package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.Osm;

import java.io.File;

public interface MapDataIOReader {
	Osm deserialize(File file) throws Exception;
}
