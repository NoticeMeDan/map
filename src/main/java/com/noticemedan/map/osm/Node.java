package com.noticemedan.map.osm;

import lombok.Data;

import java.util.List;

@Data
public class Node {
	private double lat;
	private double lon;
	private List<Tag> tags;
}
