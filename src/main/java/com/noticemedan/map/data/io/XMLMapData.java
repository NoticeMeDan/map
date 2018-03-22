package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.Osm;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;

@Slf4j
public class XMLMapData implements MapDataIOReader {
	@Override
	public Osm deserialize(InputStream inputStream) {
		Serializer serializer = new Persister();

		return Try.of(() -> serializer.read(Osm.class, inputStream))
				.onFailure(x -> log.error("Error while deserializing OSM", x))
				.getOrElse(new Osm());
	}
}
