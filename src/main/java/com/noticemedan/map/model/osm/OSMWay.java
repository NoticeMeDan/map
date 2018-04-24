package com.noticemedan.map.model.osm;

import io.vavr.collection.List;

public class OSMWay extends List<OSMNode> {
	@Override
	public int length() {
		return 0;
	}

	public OSMNode from() {
        return get(0);
    }

    public OSMNode to() {
        return get(size() - 1);
    }
}
