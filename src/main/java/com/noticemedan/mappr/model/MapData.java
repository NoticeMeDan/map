package com.noticemedan.mappr.model;

import com.noticemedan.mappr.App;
import com.noticemedan.mappr.dao.OsmDao;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Element;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
class MapData implements Serializable {
    @Getter
	private Vector<Element> osmElements;
	@Getter
	private Vector<Element> osmCoastlineElements;
	@Getter
	private Vector<Address> addresses;

    MapData() {
    	try {
			String filename = "fyn.osm.zip";
			Path file = Paths.get(App.class.getResource("/" + filename).toURI());
			OsmDao osmDao = new OsmDao();
			TestMapData data = osmDao.read(file);

			osmElements = data.getElements();
			osmCoastlineElements = data.getCoastlineElements();
			addresses = data.getAddresses();
			log.info("Size: " + osmElements.size());
			log.info("Addresses: " + addresses.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
