package com;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static Directory root;
    private static Admin admin = new Admin("admin", "admin");
    private static Account loggedInAccount = admin;
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] arg) throws IOException, ClassNotFoundException {
        System.out.println("Do you want to load a saved file system? (y/n)");
        String c = in.next();
        int allocationType;

        if (c.equalsIgnoreCase("y")) {
            System.out.println("Enter the allocation type:");
            // There is a file stored for each file system
            System.out.println("1- Contiguous Allocation" +
                    "\n2- Linked Allocation" +
                    "\nelse- Indexed Allocation");
            allocationType = in.nextInt();
            FileInputStream fileInputStream;
            if (allocationType == 1) {
                fileInputStream = new FileInputStream("contiguous.vfs");
            } else if (allocationType == 2) {
                fileInputStream = new FileInputStream("linked.vfs");
            } else {
                fileInputStream = new FileInputStream("indexed.vfs");
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            // load the root directory from the saved file system
            root = (Directory) objectInputStream.readObject();
            objectInputStream.close();
        } else {
            System.out.println("Enter the allocation type:");
            System.out.println("1- Contiguous Allocation" +
                    "\n2- Linked Allocation" +
                    "\nelse- Indexed Allocation");
            allocationType = in.nextInt();
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
        }
        in.skip("\n");
        readUsers();
        while (true) {
            System.out.print("Enter command: ");
            String command = in.nextLine();
            String[] args = command.split(" ");
            if (command.equalsIgnoreCase("exit")) {
                save(allocationType);
                break;
            } else if (args[0].equalsIgnoreCase("Login"))
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
                displayDiskStatus(args);
            else if (args[0].equalsIgnoreCase("DisplayDiskStructure"))
                displayDiskStructure(args);
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
        if (!loggedInAccount.username.equalsIgnoreCase("admin")) {
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
        if (!loggedInAccount.username.equalsIgnoreCase("admin")) {
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
            System.out.println(loggedInAccount.username);
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
                    if (user.username.equalsIgnoreCase(username)) {
                        if (user.password.equalsIgnoreCase(password)) {
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

    public static void displayDiskStatus(String[] args) {
        if (args.length == 1) {
            System.out.println("1- Empty Space: " + root.allocator.getEmptySpace());
            System.out.println("2- Allocated Space: " + root.allocator.getAllocatedSpace());
            System.out.println("3- Empty Blocks in Disk (0 For Empty): " + root.allocator.getSpace());
            System.out.println("4- Allocated Blocks in Disk for Each File:- ");
            root.displayDiskStatus();
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    public static void displayDiskStructure(String[] args) {
        if (args.length == 1) {
            root.displayDiskStructure(0);
        } else {
            System.out.println("Invalid Arguments");
        }
    }

    public static void readUsers() throws FileNotFoundException {
        Scanner scanner = new Scanner(new java.io.File("user.txt"));
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] words = line.split(",");
            admin.users.add(new User(words[0], words[1]));
        }
    }

    public static void save(int allocationType) throws IOException {
        // saves the current file system status into a file
        if (allocationType == 1) {
            FileOutputStream outputStream = new FileOutputStream("contiguous.vfs");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(root);
            objectOutputStream.close();
        } else if (allocationType == 2) {
            FileOutputStream outputStream = new FileOutputStream("linked.vfs");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(root);
            objectOutputStream.close();
        } else {
            FileOutputStream outputStream = new FileOutputStream("indexed.vfs");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(root);
            objectOutputStream.close();
        }
        FileWriter usersFileWriter = new FileWriter("user.txt", false);
        StringWriter stringWriter = new StringWriter();
        for (User user : admin.users) {
            stringWriter.write(user.username + "," + user.password + "\n");
        }
        usersFileWriter.write(stringWriter.toString());
        usersFileWriter.close();

        FileWriter capabilities = new FileWriter("capabilities.txt", false);
        StringWriter capStringWriter = new StringWriter();
        capabilitiesInfo(root, capStringWriter, "");
        capabilities.write(capStringWriter.toString());
        capabilities.close();
    }

    public static void capabilitiesInfo(Directory directory, StringWriter stringWriter, String path) {
        stringWriter.write(path + directory.getDirectoryName() + ",");
        stringWriter.write(directory.accessInfo() + "\n");
        path += directory.getDirectoryName() + "/";
        for (Directory dir : directory.getSubDirectories()) {
            capabilitiesInfo(dir, stringWriter, path);
        }
    }
}