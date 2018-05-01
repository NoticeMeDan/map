package com.noticemedan.mappr.model.util;

public class LongToOSMNodeMap {
    private Node[] table;
    int MASK;

    public LongToOSMNodeMap(int capacity) {
        table = new Node[1 << capacity];
        MASK = table.length - 1;
    }

    public void put(long id, double lon, double lat) {
        int position = Long.hashCode(id) & MASK;
        table[position] = new Node(id, lon, lat, table[position]);
    }

    public Node get(long id) {
        int position = Long.hashCode(id) & MASK;
        for (Node n = table[position]; n != null; n = n.next) {
            if (n.id == id) {
                return n;
            }
        }
        return null;
    }

    class Node extends com.noticemedan.mappr.model.osm.Node {
        long id;
        Node next;

        public Node(long id, double lon, double lat, Node n) {
            super(lon, lat);
            this.id = id;
            this.next = n;
        }

    }
}
