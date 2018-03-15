package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

/**
 * Essbkf: Elias' Super Seje Binære Kort Format
 */
public class EssbkfMapData implements MapDataIOReader, MapDataIOWriter {
	@Override
	public void serialize(File file, Osm rootNode) {

	}

	@Override
	public Osm deserialize(File file) throws XMLStreamException, IOException {
		return null;
	}
}
