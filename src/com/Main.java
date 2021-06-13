package com;

import com.Accounts.Account;
import com.Accounts.Admin;
import com.Accounts.User;
import com.Allocation.Allocator;
import com.Allocation.ContiguousAllocator;
import com.Allocation.IndexedAllocator;
import com.Allocation.LinkedAllocator;
import com.FileSystem.Directory;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static Directory root;
    private static Admin admin = new Admin("admin", "admin");
    private static Account loggedInAccount = admin;
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] arg) {
        System.out.println("Enter the allocation type:");
        System.out.println("1- Contiguous Allocation" +
                "\n2- Linked Allocation" +
                "\nelse- Indexed Allocation");
        int allocationType = in.nextInt();
        System.out.println("Enter the disk size (in KB): ");
        int N = in.nextInt();
        Allocator allocator;
        if (allocationType == 1) {
            allocator = new ContiguousAllocator(N);
            root = new Directory("root", allocator);
        } else if (allocationType == 2) {
            allocator = new LinkedAllocator(N);
            root = new Directory("root", allocator);
        } else {
            allocator = new IndexedAllocator(N);
            root = new Directory("root", allocator);
        }
        in.skip("\n");
        while (true) {
            System.out.print("Enter command: ");
            String command = in.nextLine();
            String[] args = command.split(" ");
            if (command.equalsIgnoreCase("exit"))
                break;
            else if (args[0].equalsIgnoreCase("Login"))
                login(args);
            else if (args[0].equalsIgnoreCase("CreateFile"))
                loggedInAccount.createFile(args, root);
            else if (args[0].equalsIgnoreCase("CreateFolder"))
                loggedInAccount.createFolder(args, root);
            else if (args[0].equalsIgnoreCase("DeleteFile"))
                loggedInAccount.deleteFile(args, root);
            else if (args[0].equalsIgnoreCase("DeleteFolder"))
                loggedInAccount.deleteFolder(args, root);
            else if (args[0].equalsIgnoreCase("DisplayDiskStatus"))
                loggedInAccount.displayDiskStatus(args, root);
            else if (args[0].equalsIgnoreCase("DisplayDiskStructure"))
                loggedInAccount.displayDiskStructure(args, root);
            else if (args[0].equalsIgnoreCase("TellUser"))
                tellUser(args);
            else if (args[0].equalsIgnoreCase("Grant"))
                grantAccess(args);
            else if (args[0].equalsIgnoreCase("CUser"))
                createUser(args);
            else
                System.out.println("Invalid Option!");
        }
    }

    private static void createUser(String[] args) {
        if (!loggedInAccount.getUsername().equalsIgnoreCase("admin")) {
            System.out.println("Can't create user");
            return;
        }
        if (args.length == 3) {
            admin.createUser(args[1], args[2]);
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    private static void grantAccess(String[] args) {
        if (!loggedInAccount.getUsername().equalsIgnoreCase("admin")) {
            System.out.println("Can't grant access");
            return;
        }
        if (args.length == 4) {
            String[] path = args[2].split("/");
            if (path[path.length - 1].contains(".")) {
                System.out.println("Can't grant access to files");
                return;
            }
            Directory cur = root;
            if (!path[0].equalsIgnoreCase("root")) {
                System.out.println("Path Not found");
                return;
            }
            for (int i = 0; i < path.length - 1; i++) {
                if (path[i].equalsIgnoreCase(cur.getDirectoryName())) {
                    if (cur.searchForSubDirectory(path[i + 1]) != null) {
                        cur = cur.searchForSubDirectory(path[i + 1]);
                    } else {
                        System.out.println("Path Not Found");
                        return;
                    }
                } else {
                    System.out.println("Path Not Found");
                    return;
                }
            }
            admin.grantAccess(args[1], cur, args[3]);
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    private static void tellUser(String[] args) {
        if (args.length == 1) {
            System.out.print("Current User: ");
            System.out.println(loggedInAccount.getUsername());
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    private static void login(String[] args) {
        if (args.length == 3) {
            String username = args[1];
            String password = args[2];
            if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
                loggedInAccount = admin;
                return;
            } else {
                for (User user : admin.users) {
                    if (user.getUsername().equalsIgnoreCase(username)) {
                        if (user.getPassword().equalsIgnoreCase(password)) {
                            loggedInAccount = user;
                            return;
                        }
                    }
                }
            }
            System.out.println("Username and password don't match");
        } else {
            System.out.println("Invalid arguments");
        }
    }
}