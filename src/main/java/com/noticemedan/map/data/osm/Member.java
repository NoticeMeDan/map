package com.noticemedan.map.data.osm;

import lombok.Data;
import org.simpleframework.xml.Attribute;

@Data
public class Member {
	private @Attribute String type;
	private @Attribute long ref;
	private @Attribute String role;
}
