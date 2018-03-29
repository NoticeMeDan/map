package com.noticemedan.map.data.osm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.Attribute;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private @Attribute String type;
	private @Attribute long ref;
	private @Attribute String role;
}
