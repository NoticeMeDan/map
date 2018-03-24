package com.noticemedan.map.model.model;

import java.util.List;
import java.util.Map;

public interface MapObjectBuilderInterface {
    Map<OSMType, List<MapObject>> getMapObjectsByType();
}
