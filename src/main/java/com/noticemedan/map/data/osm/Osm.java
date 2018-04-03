package com.noticemedan.map.data.osm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Root(strict = false)
public class Osm {
	private @Element Bounds bounds;

	@ElementList(inline = true, entry = "node", required = false)
	private	List<Node> nodes = new ArrayList<>();

	@ElementList(inline = true, entry = "way", required = false)
	private List<Way> ways = new ArrayList<>();

	@ElementList(inline = true, entry = "tag", required = false)
	private List<Tag> tags = new ArrayList<>();

	@ElementList(inline = true, entry = "relation", required = false)
	private List<Relation> relations = new ArrayList<>();
}
