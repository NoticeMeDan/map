package com.noticemedan.map.model;

import java.util.List;
import java.util.Map;

public interface OSMElementCreatorInterface {
	Map<OSMType, List<OSMMaterialElement>> getMapObjectsByType();

	List<OSMCoastlineElement> getListOfCoastlineObjects();
}
