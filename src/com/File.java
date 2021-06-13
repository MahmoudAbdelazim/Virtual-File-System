package com;

import java.io.Serializable;
import java.util.ArrayList;

public class File implements Serializable {
    private String fileName;
    private int size;
    private String type;
    private ArrayList<Integer> data;

    public File(String name, int size) {
        fileName = name;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public int getSize() {
        return size;
    }

    public void displayAllocatedBlocks() {
        System.out.print(fileName + "\t");
        if (type.equalsIgnoreCase("Contiguous")) // if contiguous, then we'll print the start and length
            System.out.println(data.get(0) + " " + data.get(1));
        else if (type.equalsIgnoreCase("Linked")) { // if linked, we'll print the start and end blocks
            System.out.println(data.get(0) + " " + data.get(1));
            for (int j = 2; j < data.size() - 1; j++) {
                System.out.println(data.get(j) + " " + data.get(j + 1));
            }
        } else { // if indexed, we'll print the index block number and the index block itself
            System.out.println(data.get(0));
            System.out.print(data.get(0) + "\t");
            for (int i = 1; i < data.size(); i++) {
                System.out.print(data.get(i) + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }
}
