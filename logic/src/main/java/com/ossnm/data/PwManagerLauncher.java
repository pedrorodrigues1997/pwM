package com.ossnm.data;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.net.UnknownHostException;

public class PwManagerLauncher {

    private static final Logger LOGGER = LogManager.getLogger(PwManagerLauncher.class);

    public static void main(String[] args) {
        try{
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            DB db = mongoClient.getDB( "Accounts" );
            //EngineAPI.start(db);
            //Start GUI?
            //Let it run until closed?
        }catch(UnknownHostException e){
            LOGGER.debug("Connection to DB Failed due to unknown Host", e.fillInStackTrace());
        }

    }

}