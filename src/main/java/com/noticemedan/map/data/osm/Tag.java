package com.noticemedan.map.data.osm;

import lombok.Data;
import org.simpleframework.xml.Attribute;

@Data
public class Tag {
	private @Attribute String k;
	private @Attribute String v;
}
