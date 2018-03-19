package com.noticemedan.map.osm;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Way implements Serializable {
	private List<Node> nodes;
	private List<Tag> tags;
}
