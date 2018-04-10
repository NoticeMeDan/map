package com.noticemedan.map.data.io;

import com.noticemedan.map.data.osm.OSMRootNode;
import io.vavr.control.Try;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.function.Supplier;

@Slf4j
@NoArgsConstructor
public class OSMMapData implements OSMMapDataIOReader, Supplier<OSMRootNode> {
	private InputStream osmData;

	public OSMMapData(InputStream osmData) {
		this.osmData = osmData;
	}

	@Override
	public OSMRootNode deserialize(InputStream inputStream) {
		Serializer serializer = new Persister();

		return Try.of(() -> serializer.read(OSMRootNode.class, inputStream))
				.onFailure(x -> log.error("Error while deserializing OSM", x))
				.getOrElse(new OSMRootNode());
	}

	@Override
	public OSMRootNode get() {
		return this.deserialize(this.osmData);
	}
}
