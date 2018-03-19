package com.noticemedan.map.osm;

import lombok.Data;

import java.io.Serializable;

@Data
public class Tag implements Serializable {
	private String k;
	private String v;
}
