package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.Osm;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;

public class XMLMapData implements MapDataIOReader {
	@Override
	public Osm deserialize(File file) throws Exception {
		Serializer serializer = new Persister();

		Osm rootNode = serializer.read(Osm.class, file);
		return rootNode;
	}
}
