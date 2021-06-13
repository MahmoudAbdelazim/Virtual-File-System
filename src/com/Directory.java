package com;

import java.io.Serializable;
import java.util.ArrayList;

public class Directory implements Serializable {
    private String name;
    private ArrayList<File> files;
    private ArrayList<Directory> subDirectories;
    public Allocator allocator;
    public ArrayList<Access> accesses;

    public Directory(String name, Allocator allocator) {
        files = new ArrayList<>();
        subDirectories = new ArrayList<>();
        accesses = new ArrayList<>();
        this.name = name;
        this.allocator = allocator;
    }

    public void createFile(String fileName, int size) {
        File newFile = new File(fileName, size);
        for (File file : files) { // check if file already exists
            if (file.getFileName().equalsIgnoreCase(newFile.getFileName())) {
                System.out.println("File with the same name already exists");
                return;
            }
        }
        if (!allocator.allocate(newFile)) { // check if the allocator can allocate the file
            System.out.println("Not enough space");
            return;
        }
        files.add(newFile);
        System.out.println("File created successfully");
    }

    public void createFolder(String folderName) {
        Directory subDirectory = new Directory(folderName, allocator);
        for (Directory directory : subDirectories) { // check if folder already exists
            if (directory.getDirectoryName().equalsIgnoreCase(subDirectory.getDirectoryName())) {
                System.out.println("Folder already exists");
                return;
            }
        }
        subDirectories.add(subDirectory);
        System.out.println("Folder created successfully");
    }

    public void deleteFile(String fileName) {
        for (File file : files) { // check if the file is present
            if (file.getFileName().equalsIgnoreCase(fileName)) {
                allocator.deAllocate(file); // deallocate the space of this file
                files.remove(file);
                System.out.println("File removed successfully");
                return;
            }
        }
        System.out.println("File not found");
    }

    public void deleteFolder(String folderName) {
        for (Directory directory : subDirectories) { // check if folder is present
            if (directory.getDirectoryName().equalsIgnoreCase(folderName)) {
                directory.deleteAll(); // call deleteAll() to delete it and all its subdirectories and files
                subDirectories.remove(directory);
                System.out.println("Folder removed successfully");
                return;
            }
        }
        System.out.println("Folder Not Found");
    }

    public void deleteAll() { // deletes all subdirectories and files recursively
        for (File file : files) {
            allocator.deAllocate(file);
        }
        files.clear();
        for (Directory dir : subDirectories) {
            dir.deleteAll();
        }
    }

    public void displayDiskStatus() { // displays all files allocation status recursively
        for (File file : files) {
            file.displayAllocatedBlocks();
        }
        for (Directory directory : subDirectories) {
            directory.displayDiskStatus();
        }
    }

    public void displayDiskStructure(int level) { // display the structure of the disk recursively
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(name + "/");
        for (File file : files) {
            for (int i = 0; i < level + 1; i++) {
                System.out.print("\t");
            }
            System.out.println(file.getFileName());
        }
        for (Directory directory : subDirectories) {
            directory.displayDiskStructure(level + 1);
        }
    }

    public Directory searchForSubDirectory(String name) {
        for (Directory dir : subDirectories)
            if (dir.getDirectoryName().equalsIgnoreCase(name))
                return dir;
        return null;
    }

    public String accessInfo() {
        StringBuilder res = new StringBuilder();
        for (Access acc: accesses) {
            res.append(acc.username + ",");
            if (acc.create) res.append("1");
            else res.append("0");
            if (acc.delete) res.append("1");
            else res.append("0");
        }
        return res.toString();
    }

    public String getDirectoryName() {
        return name;
    }

    public ArrayList<Directory> getSubDirectories() {
        return subDirectories;
    }
}