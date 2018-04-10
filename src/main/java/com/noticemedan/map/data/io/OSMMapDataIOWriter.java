package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.OSMRootNode;

import java.io.File;

public interface OSMMapDataIOWriter {
	void serialize(File file, OSMRootNode rootNode);
}
