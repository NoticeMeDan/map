package com.noticemedan.mappr.model.directions;

import com.noticemedan.mappr.model.NavigationAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NavigationInstruction {
	@Getter NavigationAction type;
	@Getter Double distance;
	@Getter String road;
}
