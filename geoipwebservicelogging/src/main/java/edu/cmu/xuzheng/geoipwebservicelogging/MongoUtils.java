package edu.cmu.xuzheng.geoipwebservicelogging;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * @author Xu Zheng
 * @description
 */
public class MongoUtils {
    public static MongoCollection<Document> getLoggingCollection(){
        ConnectionString connectionString = new ConnectionString("mongodb://nine077:wlds199610210017@cluster0-shard-00-01.iubdo.mongodb.net:27017/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("dashboard");
//        database.createCollection("testCollection");
        MongoCollection<Document> collection = database.getCollection("logging");
        return collection;
    }
}
