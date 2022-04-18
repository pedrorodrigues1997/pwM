package com.ossnm.data;

import com.google.common.hash.Hashing;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.util.Pair;

import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.BiConsumer;


public class userManager {

    private static final Logger LOGGER = LogManager.getLogger(userManager.class);

    public userManager() {
    }

    public static User createNewUser(String username, String password, String email, DB db) {
        //CHECK IF IT ALREAD EXISTS
        if(databaseOperations.verifyUserIsInDB(username, UserField.username, db)){
            LOGGER.info("User Already in DB - Failing User Creation");
            LOGGER.info("Logging in---------------- User:{}", username);
            return authenticateUser(username, password, db);
        }
        //CHECK PASSWORD PARAMETERs -- NEW METHOD
        if(password.length() <= 8){
            LOGGER.info("Password smaller than 8 characters. Please choose a new password");
            return null;
        }


        String passwordHash = getPasswordHash(password);
        User newUser = new User(username, passwordHash, email);
        databaseOperations.addUsertoDb(username, passwordHash, email, db);
        LOGGER.info("Finished user Creation");
        return newUser;
    }


    //Authenticate/login

    public static User authenticateUser(String username, String password, DB db){
        String hash = getPasswordHash(password);
        if(databaseOperations.isUserPasswordCorrect(username, hash, db)){
            return databaseOperations.getUserFromDB(username, password, db);

        }else{
            LOGGER.info("Wrong password for user: {}", username);
            return null;
        }
    }


    //Edit Account
    public static void editUserField(User user, String newParameter, UserField fieldToChange, DB db) {
        switch (fieldToChange) {
            case username:
            case password:
            case encryptionType:
                databaseOperations.editOneParameterDb(user.getUsername(), newParameter, fieldToChange, db);
                break;
            case email:
                databaseOperations.editOneParameterDb(user.getEmail(), newParameter, fieldToChange, db);
                break;
        }
    }


    //Delete Account

    public static void deleteUser(User user, DB db) {
        databaseOperations.removeUserFromDb(user.getUsername(), db);
        //This is deleting an account and should close the main menu
        //Deauthenticate
    }

    //add new Password combo
    public static void addNewPasswordToList(User user, String accountName, String username, String password, DB db) {
        String cipherPassword = Cryptography.encryptPassword(password);


        databaseOperations.editAddToPasswordListDb(user.getUsername(), username, accountName, cipherPassword, db);
        getUserPasswords(user, true, db);
    }

    //delete new Password combo
    public static void deletePasswordFromList(User user, String accountName, DB db) {

        databaseOperations.editRemoveFromPasswordListDb(user.getUsername(), accountName, db);
        getUserPasswords(user, true, db);
    }
    //edit new password combo


    public static HashMap<String, Pair<String, String>> getUserPasswords(User user, boolean isAuthenticated, DB db) {
        //get DB data from username. Data consists of every username and password this User has saved in DB
        final HashMap<String, Pair<String, String>> passwordList = databaseOperations.getPasswordList(user.getUsername(), db);
        if (passwordList.isEmpty()) {
            LOGGER.debug("No passwords saved for user {}", user.getUsername());
            return null;
        }
        //Decrypt the password that comes encrypted
        passwordList.forEach(new BiConsumer<String, Pair<String, String>>() {
            @Override
            public void accept(String account, Pair<String, String> stringStringPair) {
                Pair<String, String> pair = passwordList.get(account);
                String decryptedPassword = Cryptography.decryptPassword(pair.getValue());
                Pair<String, String> newPair = new Pair<>(pair.getKey(), decryptedPassword);
                passwordList.replace(account, pair, newPair);
            }
        });

        //Set passwordList in User



        return passwordList;
    }

    private static String getPasswordHash(String password) {
        String passwordHash = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        return passwordHash;
    }

    public static DB getDB() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        return mongoClient.getDB( "Accounts" );
    }
}
