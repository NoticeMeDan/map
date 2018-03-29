package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.Osm;
import io.vavr.control.Try;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;
import java.util.function.Supplier;

@Slf4j
@NoArgsConstructor
public class OsmMapData implements MapDataIOReader, Supplier<Osm> {
	private InputStream osmData;

	public OsmMapData(InputStream osmData) {
		this.osmData = osmData;
	}

	@Override
	public Osm deserialize(InputStream inputStream) {
		Serializer serializer = new Persister();

		return Try.of(() -> serializer.read(Osm.class, inputStream))
				.onFailure(x -> log.error("Error while deserializing OSM", x))
				.getOrElse(new Osm());
	}

	@Override
	public Osm get() {
		return this.deserialize(this.osmData);
	}
}
