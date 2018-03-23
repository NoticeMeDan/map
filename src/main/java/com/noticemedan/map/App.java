package com.noticemedan.map;

import com.noticemedan.map.model.MapObjectBuilder;

import java.awt.*;

public class App {
    public static void main(String[] args) {
		MapObjectBuilder mob = MapObjectBuilder.getInstance(new Dimension(800, 400));
		mob.writeOut();
	}
}
