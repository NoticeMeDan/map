package com.noticemedan.map.viewmodel;

import com.noticemedan.map.data.OSMManager;
import javafx.scene.layout.Pane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardController extends KeyAdapter {
    private OSMManager model;
    private Pane p;
    private CanvasView canvas;

    public KeyboardController(Pane p, CanvasView c, OSMManager m) {
		this.p = p;
        canvas = c;
        model = m;
        canvas.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Keyboard = " + e.getKeyChar());
        switch (e.getKeyChar()) {
            case 'x':
                canvas.toggleAntiAliasing();
                break;
            case 'w':
                canvas.pan(0, 10);
                break;
            case 'a':
                canvas.pan(10, 0);
                break;
            case 's':
                canvas.pan(0, -10);
                break;
            case 'd':
                canvas.pan(-10, 0);
                break;
            case '+':
                canvas.zoomToCenter(1.1);
                break;
            case '-':
                canvas.zoomToCenter(1/1.1);
                break;
            /*case 'o':
                model.getShapesFromFile("output.bin");
                break;
            case 'p':
                model.save("output.bin");
                break;*/
            default:
                break;
        }
    }
}
