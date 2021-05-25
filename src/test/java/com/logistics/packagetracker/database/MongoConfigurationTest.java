package com.logistics.packagetracker.database;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class MongoConfigurationTest
{
    @Autowired
    MongoConfiguration database;
    
    @BeforeEach
    public void setup()
    {
        database.connectToDB();
        MockitoAnnotations.initMocks(this);
        
    }
    
    @Test
    void connectToDB()
    {
        String url = System.getenv().get("PACKAGETRACKERDBURL");
    
        assertEquals(database.getDBSTR(), url);
        assertNotNull(url);
        assertNotEquals("", url);
    }
    
    @Test
    void getDBConnection()
    {
        assertEquals(database.getDBNAME(), ((MongoDatabase) database.connectToDB()).getName());
    }
    
    @Test
    void getDBStats()
    {
        Document rDoc = database.getDBStats();
        assertEquals(rDoc.getString("db"), "packagetracker");
        assertEquals(Integer.parseInt(rDoc.getInteger("collections").toString()), 2);
    }
}