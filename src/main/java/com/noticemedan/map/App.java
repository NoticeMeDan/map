package com.noticemedan.map;

import com.noticemedan.map.data.io.XMLMapData;
import com.noticemedan.map.data.osm.Osm;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.ZipInputStream;

@Slf4j
public class App {
    public static void main(String[] args) {
		XMLMapData xmlMapper = new XMLMapData();

		ZipInputStream inputStream = new ZipInputStream(App.class.getResourceAsStream("/smaller.osm.zip"));
		Try.of(inputStream::getNextEntry)
				.onFailure(x -> log.error("Error while getting entry in zip file", x))
				.getOrNull();

		Osm rootNode = xmlMapper.deserialize(inputStream);
		log.info("# of Nodes: " + rootNode.getNodes().size());
	}
}
