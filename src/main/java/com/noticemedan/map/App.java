package com.noticemedan.map;

import com.noticemedan.map.data.io.XmlMapData;
import com.noticemedan.map.data.osm.Osm;
import io.vavr.concurrent.Future;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipInputStream;

@Slf4j
public class App {
    public static void main(String[] args) {
		ZipInputStream inputStream = new ZipInputStream(App.class.getResourceAsStream("/smaller.osm.zip"));
		Try.of(inputStream::getNextEntry)
				.onFailure(x -> log.error("Error while getting entry in zip file", x))
				.getOrNull();

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		XmlMapData xmlMapData = new XmlMapData(inputStream);
		Future.of(executorService, xmlMapData::call)
				.onSuccess(rootNode -> log.info("# of Nodes: ", rootNode.getNodes().toString()));

//		Osm rootNode = xmlMapper.deserialize(inputStream);
//		log.info("# of Nodes: " + rootNode.getNodes().size());
	}
}
