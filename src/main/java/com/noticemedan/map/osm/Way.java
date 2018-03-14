package com.noticemedan.map.osm;

import lombok.Data;

import java.util.List;

@Data
public class Way {
	private List<Node> nodes;
	private List<Tag> tags;
}
