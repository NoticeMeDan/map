package com.noticemedan.mappr.model;

import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.user.FavoritePoi;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MapData implements Serializable {
	private static final long serialVersionUID = 1;

	private Vector<Element> elements;
	private Vector<Element> coastlineElements;
	private Vector<Address> addresses;
	private List<FavoritePoi> poi;
}
