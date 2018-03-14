package com.noticemedan.map.osm;

import lombok.Data;

import java.util.List;

@Data
public class Osm {
	private Bounds bounds;
	private List<Node> nodes;
	private List<Way> ways;
	private List<Tag> tags;
	private List<Relation> relations;
}
