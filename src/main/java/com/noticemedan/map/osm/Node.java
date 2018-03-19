package com.noticemedan.map.osm;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Node implements Serializable {
	private long id;
	// Beginning of useless data
	private boolean visible;
	private int version;
	private long changeset;
	private Date timestamp;
	private String user;
	private long uid;
	// End of useless data
	private double lat;
	private double lon;
	private List<Tag> tags;
}
