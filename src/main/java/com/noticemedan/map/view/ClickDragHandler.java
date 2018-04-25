package com.noticemedan.map.view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Source: https://stackoverflow.com/questions/41079498/javafx-differentiate-between-click-and-click-dragged
 */
public class ClickDragHandler implements EventHandler<MouseEvent> {
	private EventHandler<MouseEvent> onDraggedEventHandler;
	private EventHandler<MouseEvent> onClickedEventHandler;
	private boolean dragging = false;

	public ClickDragHandler(EventHandler<MouseEvent> onDraggedEventHandler, EventHandler<MouseEvent> onClickedEventHandler) {
		this.onDraggedEventHandler = onDraggedEventHandler;
		this.onClickedEventHandler = onClickedEventHandler;
	}

	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			dragging = false;
		}
		else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
			dragging = true;
		}
		else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			onDraggedEventHandler.handle(event);
		}
		else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			if (!dragging) {
				onClickedEventHandler.handle(event);
			}
		}
	}
}
