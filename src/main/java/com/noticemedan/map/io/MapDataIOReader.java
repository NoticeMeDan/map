package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

public interface MapDataIOReader {
	Osm deserialize(File file) throws XMLStreamException, IOException;
}
