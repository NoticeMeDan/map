package com.noticemedan.map.viewmodel;

import com.noticemedan.map.data.BufferedFileReader;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapModel implements Iterable<Path> {

    private List<Path> paths;
    private BufferedFileReader bufferedFileReader;
    private String fileName;

    public MapModel(String fileName) {
        this.paths = new ArrayList<>();
        this.bufferedFileReader = new BufferedFileReader();
        this.fileName = fileName;
        fillPathList();
    }

    private void fillPathList() {
        BufferedReader bf = bufferedFileReader.createBufferedReader(fileName);
        try {
            for (String line = bf.readLine(); line != null; bf.readLine()) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals("LINE")) {
                    double[] coordinates = new double[4];
                    for (int i = 0; i < 4; i++) {
                        coordinates[i] = Double.parseDouble(tokens[i+1]);
                    }
                    paths.add(createLinePath(coordinates));
                }
            }
        } catch (Exception ioEx) {
            ioEx.printStackTrace();
        }
    }

    private Path createLinePath(double[] coordinates) {
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
