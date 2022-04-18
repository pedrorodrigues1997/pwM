package com.ossnm.data;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class User {

    private static String username;
    private static String passwordHash;
    private static String email;
    private static boolean isAuthenticated;
    private static HashMap<String, Pair<String, String>> passwordList = new HashMap<>();



    private static final Logger LOGGER = LogManager.getLogger(User.class);

    public User(String username, String passwordHash, String email){
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        User.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        User.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        User.email = email;
    }

    public boolean isIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        User.isAuthenticated = isAuthenticated;
    }

    public HashMap<String, Pair<String, String>> getPasswordList() {
        return passwordList;
    }

    public void setPasswordList(HashMap<String, Pair<String, String>> passwordList) {
        User.passwordList = passwordList;
    }

    @Override
    public String toString() {
        return "User[username " + getUsername() + " " +
                "email" + getEmail() + " " +
                "passwordList" + getPasswordList().toString();
    }
}
