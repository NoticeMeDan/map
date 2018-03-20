package com.noticemedan.map.data.osm;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Data
@Root(strict = false)
public class Osm {
	private @Element Bounds bounds;

	@ElementList(inline = true, entry = "node", required = false)
	private	List<Node> nodes;

	@ElementList(inline = true, entry = "way", required = false)
	private List<Way> ways;

	@ElementList(inline = true, entry = "tag", required = false)
	private List<Tag> tags;

	@ElementList(inline = true, entry = "relation", required = false)
	private List<Relation> relations;
}
