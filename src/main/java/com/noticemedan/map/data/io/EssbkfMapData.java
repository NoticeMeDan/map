package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.Osm;

import java.io.File;
import java.io.InputStream;

/**
 * Essbkf: Elias' Super Seje Binære Kort Format
 */
public class EssbkfMapData implements MapDataIOReader, MapDataIOWriter {
	@Override
	public void serialize(File file, Osm rootNode) {

	}

	@Override
	public Osm deserialize(InputStream inputStream) {
		return null;
	}
}
