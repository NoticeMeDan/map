package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface MapDataIOReader {
	Osm deserialize(String path) throws XMLStreamException, IOException;
}
