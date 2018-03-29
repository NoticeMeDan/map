package com.noticemedan.map.model;

import java.util.List;
import java.util.Map;

public interface MapObjectCreaterInterface {
    Map<OSMType, List<MapObject>> getMapObjectsByType();
}
