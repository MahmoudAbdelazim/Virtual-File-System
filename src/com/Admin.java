package com;

import java.util.ArrayList;

public class Admin extends Account {
    public ArrayList<User> users = new ArrayList<>();

    public Admin(String username, String password) {
        super(username, password);
    }

    public void createUser(String username, String password) {
        for (User user : users) {
            if (user.username.equalsIgnoreCase(username)) {
                System.out.println("User already registered");
                return;
            }
        }
        User user = new User(username, password);
        users.add(user);
    }

    public void grantAccess(String username, Directory directory, String access) {
        for (User user : users) {
            if (user.username.equalsIgnoreCase(username)) {
                Access access1;
                if (access.equalsIgnoreCase("00")) {
                    access1 = new Access(false, false, username);
                } else if (access.equalsIgnoreCase("11")) {
                    access1 = new Access(true, true, username);
                } else if (access.equalsIgnoreCase("10")) {
                    access1 = new Access(true, false, username);
                } else {
                    access1 = new Access(false, true, username);
                }
                for (Access acc : directory.accesses) {
                    if (acc.username.equalsIgnoreCase(username)) {
                        acc.create = access1.create;
                        acc.delete = access1.delete;
                        return;
                    }
                }
                directory.accesses.add(access1);
                return;
            }
        }
        System.out.println("User Not Found");
    }
}