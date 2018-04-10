package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.OSMRootNode;

import java.io.InputStream;

public interface OSMMapDataIOReader {
	OSMRootNode deserialize(InputStream inputStream);
}
