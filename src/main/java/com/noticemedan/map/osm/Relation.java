package com.noticemedan.map.osm;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Relation implements Serializable {
	private List<Way> ways;
	private List<Tag> tags;
}
