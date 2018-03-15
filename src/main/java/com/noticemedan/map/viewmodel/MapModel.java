package com.noticemedan.map.viewmodel;

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
                    double[] coordinates = new double[4];
                    for (int i = 0; i < 4; i++) coordinates[i] = Double.parseDouble(tokens[i+1]);
                    paths.add(createPathShape(coordinates));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path createPathShape(double[] coordinates) {
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        LineTo lineTo = new LineTo();
        moveTo.setX(coordinates[0]); moveTo.setY(coordinates[1]);
        lineTo.setX(coordinates[2]); lineTo.setY(coordinates[3]);
        path.getElements().add(moveTo);
        path.getElements().add(lineTo);
        return path;
    }

    @Override
    public Iterator<Path> iterator() {
        return paths.iterator();
    }
}
