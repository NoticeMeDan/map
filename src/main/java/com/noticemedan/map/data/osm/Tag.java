package com.noticemedan.map.data.osm;

import lombok.Data;
import org.simpleframework.xml.Attribute;

import java.io.Serializable;

@Data
public class Tag implements Serializable {
	private @Attribute String k;
	private @Attribute String v;
}
