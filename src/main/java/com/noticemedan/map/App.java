package com.noticemedan.map;

import com.noticemedan.map.io.XMLMapData;
import com.noticemedan.map.osm.Osm;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
		XMLMapData xmlMapper = new XMLMapData();
		try {
			File file = new File(String.valueOf(ClassLoader.getSystemResource("smaller.osm")).split(":")[1]);
			file.exists();
			Osm rootNode = xmlMapper.deserialize(file);
		} catch (IOException | MarshalException | ValidationException | MappingException e) {
			e.printStackTrace();
		}
	}
}
