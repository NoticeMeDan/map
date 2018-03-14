package com.noticemedan.map.osm;

import lombok.Data;

import java.util.List;

@Data
public class Relation {
	private List<Way> ways;
	private List<Tag> tags;
}
