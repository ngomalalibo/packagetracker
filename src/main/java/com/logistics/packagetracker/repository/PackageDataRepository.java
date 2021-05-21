package com.logistics.packagetracker.repository;

import com.google.common.base.Strings;
import com.logistics.packagetracker.dataProvider.SortProperties;
import com.logistics.packagetracker.database.MongoConnection;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.exception.CustomNullPointerException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * This repository was built manually to perform most of the mongo database query operations. It uses aggregations and regular mongo commands.
 * As an alternative to Spring data mongo this repository provides all the regular operations required for data persistence and manipulation.
 */
@Slf4j
@Getter
@Repository
public class PackageDataRepository
{
    @Autowired
    private MongoConnection mongoConnection;
    
    public boolean isCollectionNullorEmpty()
    {
        if (mongoConnection.packages == null)
        {
            return true;
        }
        return mongoConnection.packages.countDocuments() <= 0;
    }
    
    public Package getObjectById(String objectId, MongoCollection<Package> collection)
    {
        AtomicReference<Package> object = new AtomicReference<>();
        Bson idQuery;
        try
        {
            if (!Objects.isNull(objectId))
            {
                idQuery = Aggregates.match(Filters.eq("_id", new ObjectId(objectId)));
                Bson limit = Aggregates.limit(1);
                Optional.of(collection.aggregate(List.of(idQuery, limit))).ifPresent(a -> object.set(a.first()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            e.getCause();
        }
        
        return object.get();
    }
    
    public List<Package> getPackageByAnyProperty(String key, String value, List<SortProperties> sortOrder)
    {
        if (!isCollectionNullorEmpty())
        {
            List<Package> searchResult = new ArrayList<>();
            Bson filter;
            if (Strings.isNullOrEmpty(value.trim()))
            {
                filter = Aggregates.match(new Document());
            }
            else
            {
                filter = Aggregates.match(Filters.eq(key, value));
            }
            if (key.equals("_id"))
            {
                filter = Aggregates.match(Filters.eq(key, new ObjectId(value)));
            }
            LinkedList<Bson> pipeline = new LinkedList<>();
            pipeline.add(filter);
            sortOrder.forEach(ps -> pipeline.add(Aggregates.sort(ps.isAscending() ? Sorts.ascending(ps.getPropertyName()) : Sorts.descending(ps.getPropertyName()))));
            
            Optional<AggregateIterable<Package>> aggregate = Optional.of(mongoConnection.packages.aggregate(pipeline));
            aggregate.get().iterator().forEachRemaining(searchResult::add);
            
            return searchResult;
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public List<Package> getAllOfEntity()
    {
        if (!isCollectionNullorEmpty())
        {
            List<Package> allRecords = new ArrayList<>();
            Optional.of(mongoConnection.packages.find()).ifPresent(s ->
                                                                   {
                                                                       s.iterator().forEachRemaining(allRecords::add);
                                                                   });
            return new ArrayList<>(allRecords);
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public boolean existsByID(String id)
    {
        Package objectById = getObjectById(id, mongoConnection.packages);
        return objectById != null;
    }
    
    public long count()
    {
        return getAllOfEntity().size();
    }
    
}
