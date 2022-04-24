package com.ossnm.data;

import com.google.common.hash.Hashing;
import com.mongodb.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class databaseOperations {
    private static final Logger LOGGER = LogManager.getLogger(databaseOperations.class);


    public static boolean addUsertoDb(String username, String passwordHash, String email, DB db) {
        DBCollection collection = db.getCollection("Clients");
        List<DBObject> array = new ArrayList<DBObject>();
        BasicDBObject doc = new BasicDBObject();
        doc.put("username", username);
        doc.put("password", passwordHash);
        doc.put("email", email);
        doc.put("passwordList", array);
        collection.insert(doc);

        return verifyUserIsInDB(username, UserField.username, db);
    }


    public static boolean removeUserFromDb(String username, DB db) {
        DBCollection collection = db.getCollection("Clients");
        BasicDBObject doc = new BasicDBObject("username", username);
        DBCursor cursor = collection.find(doc);
        if (cursor.hasNext()) {
            collection.remove(cursor.next());
            LOGGER.info("User removed from DB ----- Username: {}", username);
            return true;
        } else {
            LOGGER.info("User not found in DB ----- Username: {}", username);
            return false;
        }
    }

    public static boolean editOneParameterDb(String queryParameter, String newParameter, UserField field, DB db) {
        DBCollection collection = db.getCollection("Clients");

        if (field.toString().equals("password")) {
            BasicDBObject query = new BasicDBObject("username", queryParameter);
            String passwordHash = Hashing.sha256()
                    .hashString(newParameter, StandardCharsets.UTF_8)
                    .toString();

            BasicDBObject newDocument = new BasicDBObject(field.toString(), passwordHash);
            BasicDBObject updateObject = new BasicDBObject("$set", newDocument);
            collection.update(query, updateObject);
            return verifyUserIsInDB(newParameter, field, db);

        }

        BasicDBObject query = new BasicDBObject(field.toString(), queryParameter);
        BasicDBObject newDocument = new BasicDBObject(field.toString(), newParameter);
        BasicDBObject updateObject = new BasicDBObject("$set", newDocument);
        collection.update(query, updateObject);
        return verifyUserIsInDB(newParameter, field, db);
    }

    public static boolean editAddToPasswordListDb(String username, String usernameOfAccount, String accountName, String passwordHash, DB db) {
        DBCollection collection = db.getCollection("Clients");
        BasicDBObject query = new BasicDBObject("username", username);
        BasicDBObject newDocument = new BasicDBObject("passwordList", new BasicDBObject("AccountName", accountName).append("user", usernameOfAccount).append("password", passwordHash));
        BasicDBObject updateObject = new BasicDBObject("$push", newDocument);
        collection.update(query, updateObject);
        return true;

    }

    public static boolean editRemoveFromPasswordListDb(String username, String accountName, DB db) {
        DBCollection collection = db.getCollection("Clients");
        BasicDBObject query = new BasicDBObject("username", username);
        BasicDBObject newDocument = new BasicDBObject("passwordList", new BasicDBObject("AccountName", accountName));
        BasicDBObject updateObject = new BasicDBObject("$pull", newDocument);
        collection.update(query, updateObject);
        return true;
    }


    public static HashMap<String, Pair<String, String>> getPasswordList(String username, DB db) {
        HashMap<String, Pair<String, String>> passwordList = new HashMap<>();
        DBCollection collection = db.getCollection("Clients");
        BasicDBObject query = new BasicDBObject("username", username);
        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            BasicDBList listOfPasswords = (BasicDBList) object.get("passwordList");
            listOfPasswords.forEach(element -> {
                String user = (((BasicDBObject) element).get("user")).toString();
                String pass = (((BasicDBObject) element).get("password")).toString();
                String Account = (((BasicDBObject) element).get("AccountName")).toString();
                Pair<String, String> userPass = new Pair<>(user, pass);

                passwordList.put(Account, userPass);
            });
        }

        return passwordList;
    }

    public static boolean isUserPasswordCorrect(String username, String password, DB db) {
        DBCollection collection = db.getCollection("Clients");
        BasicDBObject userQuery = new BasicDBObject();
        userQuery.put("username", username);
        DBCursor cursor = collection.find(userQuery);
        if (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            String pass = object.get("password").toString();
            if (password.equals(pass)) {
                LOGGER.info("User {} is Authenticated", username);
                return true;
            } else {
                LOGGER.info("User {} failed authentication. Incorrect password", username);
                return false;
            }

        }else{
            LOGGER.info("User {} failed authentication. No user found", username);
            return false;
        }

    }

    public static User getUserFromDB(String username,String password, DB db) throws UnknownHostException {
        HashMap<String, Pair<String, String>> passwordList = new HashMap<>();
        DBCollection collection = db.getCollection("Clients");
        BasicDBObject userQuery = new BasicDBObject();
        userQuery.put("username", username);
        DBCursor cursor = collection.find(userQuery);
        if (cursor.hasNext()) {
            BasicDBObject object = (BasicDBObject) cursor.next();
            BasicDBList listOfPasswords = (BasicDBList) object.get("passwordList");
            listOfPasswords.forEach(element -> {
                String user = (((BasicDBObject) element).get("user")).toString();
                String pass = (((BasicDBObject) element).get("password")).toString();
                String Account = (((BasicDBObject) element).get("AccountName")).toString();

                Pair<String, String> userPass = new Pair<>(user, pass);

                passwordList.put(Account, userPass);
            });
           User user = new User(object.get("username").toString(), password, object.get("email").toString());
           user.setPasswordList();
           LOGGER.info("Fetched user {} from Database", user.toString());
           return user;

        }else{
            LOGGER.info("User {} not found", username);
            return null;
        }

    }

    public static boolean verifyUserIsInDB(String paramter, UserField field, DB db) {
        DBCollection collection = db.getCollection("Clients");
        BasicDBObject userQuery = new BasicDBObject();
        userQuery.put(field.toString(), paramter);
        DBCursor cursor = collection.find(userQuery);
        if (cursor.hasNext()) {
            LOGGER.info("Is in DB ----- {}: {}", field.toString(), paramter);
            return true;
        }
        LOGGER.debug("User not found in DB after creation --- Username {}", paramter);
        return false;
    }


}
