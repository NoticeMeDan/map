package com.noticemedan.mappr.viewmodel;

import com.noticemedan.mappr.model.DomainFacade;
import com.noticemedan.mappr.view.CanvasView;
import lombok.Getter;

public class CanvasViewController {
	@Getter
	CanvasView canvasView;

	public CanvasViewController(DomainFacade domainFacade) {
		this.canvasView = new CanvasView(domainFacade);
	}

}
