package com.noticemedan.map.viewmodel;

import com.noticemedan.map.data.BufferedFileReader;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapModel implements Iterable<Path> {

    private List<Path> paths;
    private String filename;

    public MapModel(String filename) {
        this.paths = new ArrayList<>();
        this.filename = filename;
        fillPathList();
    }

    private void fillPathList() {
        try {
            BufferedReader b = new BufferedReader(new FileReader("src/" + filename));
            for (String line = b.readLine(); line != null; line = b.readLine() ) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals("LINE")) {
                    double x1 = Double.parseDouble(tokens[1]);
                    double y1 = Double.parseDouble(tokens[2]);
                    double x2 = Double.parseDouble(tokens[3]);
                    double y2 = Double.parseDouble(tokens[4]);
                    Path path = new Path();
                    MoveTo moveTo = new MoveTo();
                    LineTo lineTo = new LineTo();
                    moveTo.setX(x1); moveTo.setY(y1);
                    lineTo.setX(x2); lineTo.setY(y2);
                    path.getElements().add(moveTo);
                    path.getElements().add(lineTo);
                    paths.add(path);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<Path> iterator() {
        return paths.iterator();
    }
}
