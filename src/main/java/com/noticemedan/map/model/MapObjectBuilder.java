package com.noticemedan.map.model;

import com.noticemedan.map.App;
import com.noticemedan.map.data.io.XMLMapData;
import com.noticemedan.map.data.osm.*;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

@Slf4j
public class MapObjectBuilder implements MapObjectBuilderInterface {
	private static MapObjectBuilder instance;
	@Getter private Bounds bounds;
	@Getter private static Map<Long, Node> nodeMap;
	@Getter private static Map<Long, Way> wayMap;
	@Getter private static Map<Long, Relation> relationMap;
	private EnumMap<OSMType, List<MapObject>> mapObjectEnumMap;
	private double minLon, minLat, maxLon, maxLat;

	private MapObjectBuilder() {
		XMLMapData xmlMapper = new XMLMapData();

		ZipInputStream inputStream = new ZipInputStream(App.class.getResourceAsStream("/small.osm.zip"));
		Try.of(inputStream::getNextEntry)
				.onFailure(x -> log.error("Error while getting entry in zip file", x))
				.getOrNull();

		Osm rootNode = xmlMapper.deserialize(inputStream);
		initializeCollections(rootNode);
		log.info("# of Nodes: " + rootNode.getNodes().size());
	}

	private void initializeCollections(Osm rootNode) {
		bounds = rootNode.getBounds();
		nodeMap = rootNode.getNodes().stream().collect(Collectors.toMap(Node::getId, x -> x));
		wayMap = rootNode.getWays().stream().collect(Collectors.toMap(Way::getId, x -> x));
		relationMap = rootNode.getRelations().stream().collect(Collectors.toMap(Relation::getId, x -> x));
	}

	public static MapObjectBuilder getInstance() {
		if (instance == null) instance = new MapObjectBuilder();
		return instance;
	}

	public void writeOut() {
		log.info(nodeMap.toString());
	}

	@Override
	public Map<OSMType, List<MapObject>> getMapObjectsByType() {
		return null;
	}


}
