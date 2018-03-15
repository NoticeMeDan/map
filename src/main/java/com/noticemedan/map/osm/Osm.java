package com.noticemedan.map.osm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"version", "generator", "copyright", "attribution", "license"})
public class Osm {
	private Bounds bounds;
	private List<Node> nodes;
	private List<Way> ways;
	private List<Tag> tags;
	private List<Relation> relations;
}
