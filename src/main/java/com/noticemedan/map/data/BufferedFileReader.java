package com.noticemedan.map.data;

import java.io.*;

public class BufferedFileReader {

    public BufferedReader createBufferedReader(String fileName) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader("src/" + fileName));
            return bf;
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        return null;
    }

}
