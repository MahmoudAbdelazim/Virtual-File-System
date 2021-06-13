package com;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Account implements Serializable {
    protected String username;
    protected String password;

    Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private boolean canCreate(Directory directory) {
        if (username.equalsIgnoreCase("admin")) return true;
        for (Access acc: directory.accesses) {
            if (acc.username.equalsIgnoreCase(username) && acc.create) {
                return true;
            }
        }
        return false;
    }

    private boolean canDelete(Directory directory) {
        if (username.equalsIgnoreCase("admin")) return true;
        for (Access acc: directory.accesses) {
            if (acc.username.equalsIgnoreCase(username) && acc.delete) {
                return true;
            }
        }
        return false;
    }

    public void createFolder(String[] args, Directory root) {
        if (args.length == 2) {
            boolean can = false;
            String[] path = args[1].split("/");
            Directory cur = root;
            if (!path[0].equalsIgnoreCase("root")) {
                System.out.println("Path Not found");
                return;
            }
            if (canCreate(root)) can = true;
            for (int i = 0; i < path.length - 2; i++) {
                if (path[i].equalsIgnoreCase(cur.getDirectoryName())) {
                    if (cur.searchForSubDirectory(path[i + 1]) != null) {
                        cur = cur.searchForSubDirectory(path[i + 1]);
                        if (canCreate(cur)) can = true;
                    } else {
                        System.out.println("Path Not Found");
                        return;
                    }
                } else {
                    System.out.println("Path Not Found");
                    return;
                }
            }
            if (can)
                cur.createFolder(path[path.length - 1]); // create the new folder
            else System.out.println("User doesn't have permission to create");
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    public void createFile(String[] args, Directory root) {
        if (args.length == 3) {
            boolean can = false;
            String[] path = args[1].split("/"); // split the path by '/' characters
            Directory cur = root; // start from the root
            if (!path[0].equalsIgnoreCase("root")) { // first word in the path must be root
                System.out.println("Path Not found2");
                return;
            }
            if (canCreate(root)) can = true;
            for (int i = 0; i < path.length - 2; i++) { // keep going forward in path and check if it's valid
                if (path[i].equalsIgnoreCase(cur.getDirectoryName())) {
                    if (cur.searchForSubDirectory(path[i + 1]) != null) {
                        cur = cur.searchForSubDirectory(path[i + 1]);
                        if (canCreate(cur)) can = true;
                    } else {
                        System.out.println("Path Not Found");
                        return;
                    }
                } else {
                    System.out.println("Path Not Found");
                    return;
                }
            }
            String size = args[args.length - 1]; // the size of the new file
            for (int i = 0; i < size.length(); i++) {
                if (!Character.isDigit(size.charAt(i))) { // size must only contain digits
                    System.out.println("Size is not valid");
                    return;
                }
            }
            if (can) cur.createFile(path[path.length - 1], Integer.parseInt(size)); // create the new file
            else System.out.println("User doesn't have permission to create");
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    public void deleteFile(String[] args, Directory root) {
        if (args.length == 2) {
            boolean can = false;
            String[] path = args[1].split("/");
            Directory cur = root;
            if (!path[0].equalsIgnoreCase("root")) {
                System.out.println("Path Not found");
                return;
            }
            if (canDelete(root)) can = true;
            for (int i = 0; i < path.length - 2; i++) {
                if (path[i].equalsIgnoreCase(cur.getDirectoryName())) {
                    if (cur.searchForSubDirectory(path[i + 1]) != null) {
                        cur = cur.searchForSubDirectory(path[i + 1]);
                        if (canDelete(cur)) can = true;
                    } else {
                        System.out.println("Path Not Found");
                        return;
                    }
                } else {
                    System.out.println("Path Not Found");
                    return;
                }
            }
            if (can) cur.deleteFile(path[path.length - 1]); // delete the specified file
            else System.out.println("User doesn't have permission to delete");
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    public void deleteFolder(String[] args, Directory root) {
        if (args.length == 2) {
            boolean can = false;
            String[] path = args[1].split("/");
            Directory cur = root;
            if (!path[0].equalsIgnoreCase("root")) {
                System.out.println("Path Not found");
                return;
            }
            if (canDelete(root)) can = true;
            for (int i = 0; i < path.length - 2; i++) {
                if (path[i].equalsIgnoreCase(cur.getDirectoryName())) {
                    if (cur.searchForSubDirectory(path[i + 1]) != null) {
                        cur = cur.searchForSubDirectory(path[i + 1]);
                        if (canDelete(cur)) can = true;
                    } else {
                        System.out.println("Path Not Found");
                        return;
                    }
                } else {
                    System.out.println("Path Not Found");
                    return;
                }
            }
            if (can) cur.deleteFolder(path[path.length - 1]); // delete the specified folder
            else System.out.println("User doesn't have permission to delete");
        } else {
            System.out.println("Invalid Arguments");
        }
    }
}
