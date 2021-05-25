package com.logistics.packagetracker.database;

import com.logistics.packagetracker.codec.PackageStatusCodec;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Getter
@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration
{
    /**
     * Connect to mongo database using database url stored in system variables. Spring Data Mongo has some bugs and query limitations so I by-passes using Spring data mongo
     * to enjoy the full power of aggregations.
     */
    private final String DBNAME = "packagetracker";
    private final String DB_ORGANIZATION = "Package Tracker.";
    private final String DB_PACKAGES = "packages";
    private final String DB_TRACKER = "tracker";
    private MongoClient mongo = null;
    private MongoDatabase db;
    private MongoCollection<Package> packages;
    private MongoCollection<Document> collection;
    private MongoCollection<TrackingDetail> tracker;
    
    
    private String DBSTR = System.getenv().get("PACKAGETRACKERDBURL");
    private HashSet<String> cols = new HashSet<>();
    
    @Override
    protected String getDatabaseName()
    {
        return DBNAME;
    }
    
    @Override
    public MongoClient mongoClient()
    {
        final CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        final CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                                                                 .register("com.logistics.packagetracker.entity", "com.logistics.packagetracker.enumeration").automatic(true).build();
        final CodecRegistry cvePojoCodecRegistry = CodecRegistries.fromProviders(pojoCodecProvider);
        final CodecRegistry customEnumCodecs = CodecRegistries.fromCodecs(new PackageStatusCodec());
        
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(defaultCodecRegistry, customEnumCodecs, cvePojoCodecRegistry);
        
        ConnectionString connectionString = new ConnectionString(DBSTR);
        
        MongoClientSettings settings = MongoClientSettings.builder()
                                                          .applyConnectionString(connectionString)
                                                          .retryWrites(true)
                                                          .codecRegistry(codecRegistry)
                                                          .build();
        
        if (db == null)
        {
            mongo = MongoClients.create(settings);
            db = mongo.getDatabase(DBNAME);
            getDBStats();
        }
        
        packages = db.getCollection(DB_PACKAGES, Package.class).withCodecRegistry(codecRegistry);
        collection = db.getCollection(DB_PACKAGES, Document.class).withCodecRegistry(codecRegistry);
        tracker = db.getCollection(DB_TRACKER, TrackingDetail.class).withCodecRegistry(codecRegistry);
        
        
        return MongoClients.create(settings);
    }
    
    @Override
    public Collection getMappingBasePackages()
    {
        return Collections.singleton("com.logistics.packagetracker");
    }
    
    /**
     * Codecs are use to tell mongo how to handle conversion to and from java objects
     */
    
    // Connect to database
    public MongoDatabase connectToDB()
    {
        return db;
    }
    
    // disconnect from database at context shutdown
    public void disconnectFromDB()
    {
        if (mongo != null)
        {
            mongo.close();
        }
        mongo = null;
        db = null;
    }
    
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
    
    @EventListener(ContextStoppedEvent.class)
    public void disconnectDB()
    {
        disconnectFromDB();
        System.out.println("Disconnected from database");
    }
    
}
