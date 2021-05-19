package com.logistics.packagetracker.database;

import com.logistics.packagetracker.entity.Package;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Slf4j
@Getter
@Component
public class MongoConnection
{
    protected final String DBNAME = "packagetracker";
    protected static final String DB_ORGANIZATION = "Package Tracker.";
    protected static final String DB_TRACKER = "tracker";
    protected static MongoClient mongo = null;
    protected static MongoDatabase db;
    public MongoCollection<Package> tracker;
    
    protected String DBSTR = System.getenv().get("PACKAGETRACKERDBURL");
    private HashSet<String> cols = new HashSet<>();
    
    public MongoConnection()
    {
        connectToDB();
    }
    
    public static CodecRegistry getCodecRegistry()
    {
        final CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        final CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                                                                 .register("com.logistics.packagetracker.entity", "com.logistics.packagetracker.enumeration").automatic(true).build();
        final CodecRegistry cvePojoCodecRegistry = CodecRegistries.fromProviders(pojoCodecProvider);
        return CodecRegistries.fromRegistries(defaultCodecRegistry, cvePojoCodecRegistry);
    }
    
    public MongoDatabase connectToDB()
    {
        ConnectionString connectionString = new ConnectionString(DBSTR);
        
        MongoClientSettings settings = MongoClientSettings.builder()
                                                          .applyConnectionString(connectionString)
                                                          .retryWrites(true)
                                                          .codecRegistry(getCodecRegistry())
                                                          .build();
        CodecRegistry pojoCodecRegistry = getCodecRegistry();
        
        if (db == null)
        {
            mongo = MongoClients.create(settings);
            db = mongo.getDatabase(DBNAME);
            getDBStats();
        }
        
        tracker = db.getCollection(DB_TRACKER, Package.class).withCodecRegistry(pojoCodecRegistry);
        return db;
    }
    
    public void disconnectFromDB()
    {
        if (mongo != null)
        {
            mongo.close();
        }
        mongo = null;
        db = null;
    }
    
    /*@Override
    public void contextDestroyed(ServletContextEvent s)
    {
        log.info(" -> contextDestroyed");
        stopDB();
    }*/
    public MongoDatabase getDBConnection()
    {
        if (db == null || mongo == null)
        {
            mongo = MongoClients.create(DBSTR);
            db = mongo.getDatabase(DBNAME);
        }
        return db;
    }
    
    public Document getDBStats()
    {
        MongoDatabase ds = getDBConnection();
        Document stats = ds.runCommand(new Document("dbstats", 1024));
        System.out.println("DBStats: " + stats.toJson());
        
        return stats;
    }
    
    
    public void createCollection(HashSet<String> hash, String collection)
    {
        if (!hash.contains(collection))
        {
            db.createCollection(collection);
            hash.add(collection);
        }
    }
    
    @EventListener(ContextStoppedEvent.class)
    public void disconnectDB()
    {
        disconnectFromDB();
        System.out.println("Disconnected from database");
    }
    
}
