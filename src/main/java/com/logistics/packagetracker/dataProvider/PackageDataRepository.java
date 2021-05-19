package com.logistics.packagetracker.dataProvider;

import com.google.common.base.Strings;
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
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Getter
@Repository
public class PackageDataRepository
{
    private MongoConnection db;
    
    public boolean isCollectionNullorEmpty()
    {
        if (db.tracker == null)
        {
            return true;
        }
        return db.tracker.countDocuments() <= 0;
    }
    
    public PackageDataRepository()
    {
        db = new MongoConnection();
    }
    
    public static java.lang.Package getObjectById(String objectId, MongoCollection<java.lang.Package> collection)
    {
        AtomicReference<java.lang.Package> object = new AtomicReference<>();
        Bson idQuery;
        try
        {
            if (!Objects.isNull(objectId))
            {
                idQuery = Aggregates.match(Filters.eq("_id", objectId));
                Optional.of(collection.aggregate(Collections.singletonList(idQuery))).ifPresent(a -> object.set(a.first()));
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
            LinkedList<Bson> pipeline = new LinkedList<>();
            pipeline.add(filter);
            sortOrder.forEach(ps -> pipeline.add(Aggregates.sort(ps.isAscending() ? Sorts.ascending(ps.getPropertyName()) : Sorts.descending(ps.getPropertyName()))));
            
            Optional<AggregateIterable<Package>> aggregate = Optional.of(db.tracker.aggregate(pipeline));
            aggregate.get().iterator().forEachRemaining(searchResult::add);
            
            return searchResult;
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public int getPackageByKeyCount(String key, String value)
    {
        if (!isCollectionNullorEmpty())
        {
            if (!Objects.isNull(key) && !Objects.isNull(value))
            {
                List<SortProperties> sortOrder = new ArrayList<>();
                return getPackageByAnyProperty(key, value, sortOrder).size();
            }
            else
            {
                throw new CustomNullPointerException("attempting to get Entity By key count on non-existent key/value");
            }
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public Stream<Package> getAllOfEntity()
    {
        if (!isCollectionNullorEmpty())
        {
            //collection is already set by constructor
            List<Package> allRecords = new ArrayList<>();
            Optional.of(db.tracker.find()).ifPresent(s ->
                                                                     {
                                                                         s.iterator().forEachRemaining(allRecords::add);
                                                                     });
            return allRecords.stream();
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
        
    }
    
    public List<Package> getPackageByTwoFilterSearch(List<SortProperties> sort, String filterOne, String filterTwo, String name)
    {
        /*System.out.println("filterOne = " + filterOne);
        System.out.println("filterTwo = " + filterTwo);
        System.out.println("name = " + name);*/
        if (!isCollectionNullorEmpty())
        {
            List<Package> searchResult = new ArrayList<>();
            Bson match;
            if (Objects.isNull(name.trim()))
            {
                //fetch all documents. Empty filter
                match = Aggregates.match(new Document());
            }
            else
            {
                Pattern ptrn = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                match = Aggregates.match(Filters.or(Filters.regex(filterOne, ptrn), Filters.regex(filterTwo, ptrn)));
            }
            LinkedList<Bson> pipeline = new LinkedList<>();
            pipeline.add(match);
            sort.forEach(ps -> pipeline.add(Aggregates.sort(ps.isAscending() ? Sorts.ascending(ps.getPropertyName()) : Sorts.descending(ps.getPropertyName()))));
            
            Optional<AggregateIterable<Package>> aggregate = Optional.of(db.tracker.aggregate(pipeline));
            aggregate.get().iterator().forEachRemaining(searchResult::add);
            
            return searchResult;
        }
        else
        {
            throw new CustomNullPointerException("Collection is null or empty");
        }
    }
}
