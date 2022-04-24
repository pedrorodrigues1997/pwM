package com.ossnm.data;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;
import java.util.HashMap;

import static com.ossnm.data.userManager.getDB;
import static com.ossnm.data.userManager.getUserPasswords;

public class User {

    private String username;
    private String passwordHash;
    private String email;
    private boolean isAuthenticated;
    private HashMap<String, Pair<String, String>> passwordList = new HashMap<>();



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
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public HashMap<String, Pair<String, String>> getPasswordList() {
        return passwordList;
    }

    public void setPasswordList() throws UnknownHostException {
        this.passwordList = getUserPasswords(this, true, getDB());
    }

    @Override
    public String toString() {
        return "User[username " + getUsername() + " " +
                "email" + getEmail() + " " +
                "passwordList" + getPasswordList().toString();
    }
}
