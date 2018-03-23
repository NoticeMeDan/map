package com.noticemedan.map;

import com.noticemedan.map.data.io.XmlMapData;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.zip.ZipInputStream;

@Slf4j
public class App {
    public static void main(String[] args) {
		ZipInputStream inputStream = new ZipInputStream(App.class.getResourceAsStream("/smaller.osm.zip"));
		Try.of(inputStream::getNextEntry)
				.onFailure(x -> log.error("Error while getting entry in zip file", x))
				.getOrNull();

		XmlMapData xmlMapData = new XmlMapData(inputStream);
		CompletableFuture.supplyAsync(xmlMapData, Executors.newSingleThreadExecutor())
				.thenAccept(rootNode -> log.info("# of Nodes: " + rootNode.getNodes().size()));

		log.info("Hi mom, show me first");
	}
}
