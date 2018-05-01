package com.noticemedan.mappr.model;

import com.noticemedan.mappr.App;
import com.noticemedan.mappr.dao.OsmDao;
import com.noticemedan.mappr.model.osm.Address;
import com.noticemedan.mappr.model.osm.Element;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.ZipInputStream;

@Slf4j
public class MapData implements Serializable {
    @Getter
	private Vector<Element> osmElements;
	@Getter
	private Vector<Element> osmCoastlineElements;
	@Getter
	private Vector<Address> addresses;

    public MapData() {
    	try {
			String filename = "fyn.osm.zip";
			File file = new File(App.class.getResource("/" + filename).getFile());
			ZipInputStream inputStream = new ZipInputStream(new FileInputStream(file));
			inputStream.getNextEntry();
			OsmDao osmDao = new OsmDao();
			TestMapData data = osmDao.read(inputStream);

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
