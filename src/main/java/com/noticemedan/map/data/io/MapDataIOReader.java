package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.Osm;

import java.io.InputStream;

public interface MapDataIOReader {
	Osm deserialize(InputStream inputStream);
}
