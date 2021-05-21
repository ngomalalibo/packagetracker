package com.logistics.packagetracker.serviceimpl;

import com.google.common.base.Strings;
import com.logistics.packagetracker.aspect.Loggable;
import com.logistics.packagetracker.dataProvider.SortProperties;
import com.logistics.packagetracker.database.MongoConnection;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityNotFoundException;
import com.logistics.packagetracker.exception.PackageStateException;
import com.logistics.packagetracker.repository.PackageDataRepository;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Projections.*;

/**
 * This is the implementation of the Package tracking service specification. It corroborates the services offered in the package tracking API.
 */

@Service
public class PackageServiceImpl implements PackageService
{
    @Autowired
    private PackageDataRepository packageDataRepository;
    
    @Autowired
    private MongoConnection mongoConnection;
    
    public PackageDataRepository getPackageDataRepository()
    {
        return packageDataRepository;
    }
    
    public MongoConnection getMongoConnection()
    {
        return mongoConnection;
    }
    
    @Override
    public List<Package> findAllPackages()
    {
        return packageDataRepository.getAllOfEntity();
    }
    
    @Override
    public Package getPackageById(String id)
    {
        if (Strings.isNullOrEmpty(id))
        {
            throw new EntityNotFoundException("Provide a valid package ID");
        }
        return packageDataRepository.getObjectById(id, mongoConnection.packages);
    }
    
    @Override
    public boolean existsById(String id)
    {
        return packageDataRepository.existsByID(id);
    }
    
    @Override
    public long count()
    {
        return packageDataRepository.count();
    }
    
    @Override
    public String trackPackage(TrackingDetail track, String id)
    {
        if (track != null && !Strings.isNullOrEmpty(id))
        {
            if (isPickedUp(id) && track.getStatus() == PackageStatus.PICKED_UP)
            {
                throw new PackageStateException("Package has already been picked up.");
            }
            else if (isDelivered(id) && track.getStatus() == PackageStatus.DELIVERED)
            {
                throw new PackageStateException("Package has already been delivered.");
            }
            
            Bson match = Filters.eq("_id", new ObjectId(id));
            track.setDateTime(System.currentTimeMillis());
            Bson updArray = Updates.combine(Updates.push("trackingDetails", track), Updates.set("status", track.getStatus()));
            UpdateResult updateResult = mongoConnection.packages.updateOne(match, updArray);
            if (updateResult.getModifiedCount() >= 1)
            {
                return id;
            }
            else
            {
                throw new EntityNotFoundException("Could not update package with ID: " + id);
            }
        }
        else
        {
            throw new EntityNotFoundException("Provide package to track");
        }
    }
    
    @Loggable
    @Override
    public Package createPackage(Package entity)
    {
        // Manual auditing for now. Hibernate Envers or JPA auditing annotations should be used with Spring Data
        if (entity == null)
        {
            throw new EntityNotFoundException("Package not created. Provide package information in request body.");
        }
        if (entity.getTrackingDetails() != null && entity.getTrackingDetails().size() != 0 && entity.getTrackingDetails().get(0) != null)
        {
            entity.getTrackingDetails().get(0).setDateTime(System.currentTimeMillis());
        }
        else
        {
            throw new EntityNotFoundException("Package not created. Provide tracker details along with package information in request body.");
        }
        entity.setCreatedDate(ZonedDateTime.now().format(DateConverter.formatter));
        InsertOneResult result = mongoConnection.packages.insertOne(entity);
        if (result.getInsertedId() != null)
        {
            ObjectId value = result.getInsertedId().asObjectId().getValue();
            entity.setId(value.toHexString());
            return entity;
        }
        return null;
    }
    
    @Override
    public boolean isPickedUp(String id)
    {
        Bson match = Aggregates.match(Filters.and(Filters.eq("_id", new ObjectId(id))));
        Bson unwind = Aggregates.unwind("$trackingDetails");
        Bson proj = Aggregates.project(fields(include("trackingDetails"), excludeId()));
        Bson match2 = Aggregates.match(Filters.in("trackingDetails.status", List.of(PackageStatus.PICKED_UP.toString())));
        List<Bson> pipeline = List.of(match, unwind, proj, match2);
        AggregateIterable<Document> aggregate = mongoConnection.collection.aggregate(pipeline);
        if (aggregate.iterator().hasNext())
        {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isDelivered(String id)
    {
        Bson match = Aggregates.match(Filters.and(Filters.eq("_id", new ObjectId(id))));
        Bson unwind = Aggregates.unwind("$trackingDetails");
        Bson proj = Aggregates.project(fields(include("trackingDetails"), excludeId()));
        Bson match2 = Aggregates.match(Filters.in("trackingDetails.status", List.of(PackageStatus.DELIVERED.toString())));
        List<Bson> pipeline = List.of(match, unwind, proj, match2);
        AggregateIterable<Document> aggregate = mongoConnection.collection.aggregate(pipeline);
        if (aggregate.iterator().hasNext())
        {
            return true;
        }
        return false;
    }
    
    @Override
    public List<Package> findByStatus(String status)
    {
        // check if string passed is in the PackageStatus enumeration
        boolean isValid = Arrays.stream(PackageStatus.values()).anyMatch(e -> e.toString().equals(status));
        if (isValid)
        {
            return packageDataRepository.getPackageByAnyProperty("status", status.toString(), List.of(new SortProperties("createdDate", false)));
        }
        throw new PackageStateException("Provide valid package status. Package status should be either PICKED_UP, IN_TRANSIT, WAREHOUSE or DELIVERED");
    }
    
    @Override
    public TrackingDetail getCurrentTracker(String id)
    {
        
        Bson match = Aggregates.match(Filters.eq("_id", new ObjectId(id)));
        Bson unwind = Aggregates.unwind("$trackingDetails");
        Bson projB = Aggregates.project(fields(include("trackingDetails"), excludeId()));
        Bson sort = Aggregates.sort(Sorts.descending("trackingDetails.dateTime"));
        Bson limit = Aggregates.limit(1);
        
        List<Bson> pipeline = List.of(match, unwind, projB, sort, limit);
        
        AggregateIterable<Document> aggregate = mongoConnection.collection.aggregate(pipeline);
        TrackingDetail td = new TrackingDetail();
        MongoCursor<Document> iterator = aggregate.iterator();
        if (aggregate.iterator().hasNext())
        {
            Document next = iterator.next();
            Document s = (Document) next.get("trackingDetails");
            td = new TrackingDetail(PackageStatus.valueOf(s.getString("status")),
                                    s.getLong("dateTime"),
                                    s.getString("source"),
                                    s.getString("city"),
                                    s.getString("state"), s.getString("country"), s.getString("zip"));
        }
        
        return td;
    }
}
