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
public class Relation {
	@Attribute long id;

	@ElementList(inline = true, entry = "member", required = false)
	private List<Member> members;

	@ElementList(inline = true, entry = "tag", required = false)
	private List<Tag> tags;
}
