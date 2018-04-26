package com.noticemedan.map.model.osm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class OSMNode {
	@Getter
    private double lon, lat;
}
