package com.noticemedan.map.model;

import com.noticemedan.map.model.service.TextSearch;

public class DomainFacade {
	// Domain data
	public MapData mapData;

	// Services
	private TextSearch addressSearch;

	public DomainFacade() {
		this.mapData = new MapData();
	}
}
