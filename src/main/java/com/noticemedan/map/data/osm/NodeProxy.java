package com.noticemedan.map.data.osm;

import lombok.Data;
import org.simpleframework.xml.Attribute;

@Data
public class NodeProxy {
	@Attribute
	private long ref;
}
