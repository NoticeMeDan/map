package com.noticemedan.map.data.osm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
	private @Attribute String k;
	private @Attribute String v;
}
