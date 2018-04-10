package com.noticemedan.map.model.osm;

public class OSMNode {
    private double lon, lat;

    public OSMNode(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
