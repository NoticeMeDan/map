package com.noticemedan.map.model;

import com.noticemedan.map.model.osm.Address;
import com.noticemedan.map.model.osm.Element;
import io.vavr.collection.Vector;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class TestMapData {
	private Vector<Element> elements;
	private Vector<Element> coastlineElements;
	private Vector<Address> addresses;
}
