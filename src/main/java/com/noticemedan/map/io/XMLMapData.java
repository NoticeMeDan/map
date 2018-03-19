package com.noticemedan.map.io;

import com.noticemedan.map.osm.Osm;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

import java.io.*;

public class XMLMapData implements MapDataIOReader {
	@Override
	public Osm deserialize(File file) throws IOException, MappingException, MarshalException, ValidationException {
		// Load mapping
		Mapping mapping = new Mapping();
		mapping.loadMapping(String.valueOf(ClassLoader.getSystemResource("OsmMapping.xml")));

		// Init and configure XMLContext
		XMLContext context = new XMLContext();
		context.addMapping(mapping);

		FileReader reader = new FileReader(file);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setIgnoreExtraAttributes(true);
		unmarshaller.setClass(Osm.class);

		Osm rootNode = (Osm) unmarshaller.unmarshal(reader);
		return rootNode;
	}
}
