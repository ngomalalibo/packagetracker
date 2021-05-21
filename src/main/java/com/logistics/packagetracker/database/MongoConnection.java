package com.logistics.packagetracker.database;

import com.logistics.packagetracker.codec.PackageStatusCodec;
import com.logistics.packagetracker.codec.StringObjectIdCodec;
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
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyModelBuilder;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Slf4j
@Getter
@Component
public class MongoConnection
{
    /**
     * Connect to mongo database using database url stored in system variables. Spring Data Mongo has some bugs and query limitations so I by-passes using Spring data mongo
     * to enjoy the full power of aggregations.
     */
    private final String DBNAME = "packagetracker";
    private final String DB_ORGANIZATION = "Package Tracker.";
    private final String DB_PACKAGES = "packages";
    private MongoClient mongo = null;
    private MongoDatabase db;
    public MongoCollection<Package> packages;
    public MongoCollection<Document> collection;
    
    private String DBSTR = System.getenv().get("PACKAGETRACKERDBURL");
    private HashSet<String> cols = new HashSet<>();
    
    public MongoConnection()
    {
        connectToDB();
    }
    
    /**
     * Codecs are use to tell mongo how to handle conversion to and from java objects
     */
    public static CodecRegistry getCodecRegistry()
    {
        ClassModelBuilder<Package> classModelBuilder = ClassModel.builder(Package.class);
        PropertyModelBuilder<String> idPropertyModelBuilder =
                (PropertyModelBuilder<String>) classModelBuilder.getProperty("id");
        idPropertyModelBuilder.codec(new StringObjectIdCodec());
        
        final CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        final CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(classModelBuilder.build())
                                                                 .register("com.logistics.packagetracker.entity", "com.logistics.packagetracker.enumeration").automatic(true).build();
        final CodecRegistry cvePojoCodecRegistry = CodecRegistries.fromProviders(pojoCodecProvider);
        final CodecRegistry customEnumCodecs = CodecRegistries.fromCodecs(new PackageStatusCodec());
        return CodecRegistries.fromRegistries(defaultCodecRegistry, customEnumCodecs, cvePojoCodecRegistry);
    }
    
    // Connect to database
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
        
        packages = db.getCollection(DB_PACKAGES, Package.class).withCodecRegistry(pojoCodecRegistry);
        collection = db.getCollection(DB_PACKAGES, Document.class).withCodecRegistry(pojoCodecRegistry);
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
