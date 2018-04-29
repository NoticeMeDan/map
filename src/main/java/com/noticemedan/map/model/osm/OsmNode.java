package com.noticemedan.map.model.osm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OsmNode {
    private double lon, lat;
}
