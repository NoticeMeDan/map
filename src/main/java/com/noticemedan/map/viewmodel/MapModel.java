package com.noticemedan.map.viewmodel;

import javafx.scene.shape.Line;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapModel {

    private String filename;
    @Getter private List<Line> lines;

    public MapModel(){
        this.filename = "lines20k.txt";
        this.lines = new ArrayList<>();
        readFromFile();
    }

    private void readFromFile() {
        try {
            BufferedReader b = new BufferedReader(new FileReader("src/" + this.filename));
            for (String line = b.readLine(); line != null; line = b.readLine() ) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals("LINE")) {
                    double[] coordinates = new double[4];
                    for (int i = 0; i < 4; i++) coordinates[i] = Double.parseDouble(tokens[i+1]);
                    lines.add(createLineShape(coordinates));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Line createLineShape(double[] coordinates) {
        return new Line(coordinates[0],coordinates[1],coordinates[2],coordinates[3]);
    }
}
