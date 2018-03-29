package com.noticemedan.map.data.osm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Root(strict = false)
public class Node {
	private @Attribute long id;
	private @Attribute double lat;
	private @Attribute double lon;

	@ElementList(inline = true, entry = "tag", required = false)
	private List<Tag> tags;
}
