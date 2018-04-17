package com.noticemedan.map.model.osm;

import java.util.ArrayList;

public class OSMWay extends ArrayList<OSMNode>{
    public OSMNode from() {
        return get(0);
    }

    public OSMNode to() {
        return get(size() - 1);
    }
}
