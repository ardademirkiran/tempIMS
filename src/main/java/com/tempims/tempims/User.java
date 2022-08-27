package com.tempims.tempims;

public class User {
    public String username;
    public String permissions;

    public User(String username, String permissions) {
        this.username = username;
        this.permissions = permissions;
    }

    public static void changePermissions(String username, String permissions) {
        //sql part to insert new permissions string to USERS db by using username
    }

}
