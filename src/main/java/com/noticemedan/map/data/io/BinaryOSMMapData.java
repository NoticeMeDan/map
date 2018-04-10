package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.OSMRootNode;

import java.io.File;
import java.io.InputStream;

/**
 * Essbkf: Elias' Super Seje Binære Kort Format <- Sorry, Elias
 */
public class BinaryOSMMapData implements OSMMapDataIOReader, OSMMapDataIOWriter {
	@Override
	public void serialize(File file, OSMRootNode rootNode) {

	}

	@Override
	public OSMRootNode deserialize(InputStream inputStream) {
		return null;
	}
}
