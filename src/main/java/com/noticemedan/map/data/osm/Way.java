package com.noticemedan.map.data.osm;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Data
@Root(strict = false)
public class Way{
	@Attribute
	private long id;

	@ElementList(inline = true, entry = "nd")
	private List<NodeProxy> nodeRefs;

	@ElementList(inline = true, entry = "tag", required = false)
	private List<Tag> tags;

}
