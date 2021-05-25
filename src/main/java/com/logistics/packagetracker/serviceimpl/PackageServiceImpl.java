package com.logistics.packagetracker.serviceimpl;

import com.google.common.base.Strings;
import com.logistics.packagetracker.aspect.Loggable;
import com.logistics.packagetracker.database.MongoConfiguration;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.entity.TrackingDetailDTO;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityNotFoundException;
import com.logistics.packagetracker.exception.PackageStateException;
import com.logistics.packagetracker.mapper.TrackerMapper;
import com.logistics.packagetracker.repository.PackageRepository;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private PackageRepository packageRepository;
    
    @Autowired
    private MongoConfiguration mongoConfiguration;
    
    @Autowired
    private TrackerMapper trackerMapper;
    
    @Autowired
    MongoTemplate mongoTemplate;
    
    
    @Override
    public List<Package> findAllPackages()
    {
        return packageRepository.findAll();
    }
    
    @Override
    public Package getPackageById(String id)
    {
        if (Strings.isNullOrEmpty(id))
        {
            throw new EntityNotFoundException("Provide a valid package ID");
        }
        return packageRepository.findById(id).orElse(null);
    }
    
    @Override
    public boolean existsById(String id)
    {
        return packageRepository.existsById(id);
    }
    
    @Override
    public long count()
    {
        return packageRepository.count();
    }
     
    @Override
    public String trackPackage(TrackingDetail track, String id)
    {
        if (track != null && !Strings.isNullOrEmpty(id))
        {
            if (isDelivered(id))
            {
                throw new PackageStateException("Package has already been delivered.");
            }
            else if (isPickedUp(id) && track.getStatus() == PackageStatus.PICKED_UP)
            {
                throw new PackageStateException("Package has already been picked up.");
            }
            
            Query query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
            
            track.setDateTime(System.currentTimeMillis());
            
            Update update = new Update();
            update.push("trackingDetails", track);
            update.set("status", track.getStatus());
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, mongoConfiguration.getDB_PACKAGES());
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
        Package insert = mongoTemplate.insert(entity, mongoConfiguration.getDB_PACKAGES());
        System.out.println("insert ID:        " + insert.getId());
        if (insert.getId() != null)
        {
            ObjectId value = new ObjectId(insert.getId());
            ;
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
        AggregateIterable<Document> aggregate = mongoConfiguration.getCollection().aggregate(pipeline);
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
        AggregateIterable<Document> aggregate = mongoConfiguration.getCollection().aggregate(pipeline);
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
            Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
            return packageRepository.findByStatus(status, sort).orElse(null);
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
        
        AggregateIterable<Document> aggregate = mongoConfiguration.getCollection().aggregate(pipeline);
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
    
    @Override
    public List<TrackingDetailDTO> getPackageTrackingHistory(String id)
    {
        Bson match = Aggregates.match(Filters.eq("_id", new ObjectId(id)));
        Bson unwind = Aggregates.unwind("$trackingDetails");
        Bson projB = Aggregates.project(fields(include("trackingDetails"), excludeId()));
        Bson sort = Aggregates.sort(Sorts.descending("trackingDetails.dateTime"));
        
        List<Bson> pipeline = List.of(match, unwind, projB, sort);
        
        AggregateIterable<Document> aggregate = mongoConfiguration.getCollection().aggregate(pipeline);
        List<TrackingDetailDTO> tdList = new ArrayList<>();
        MongoCursor<Document> iterator = aggregate.iterator();
        if (aggregate.iterator().hasNext())
        {
            Document next = iterator.next();
            Document s = (Document) next.get("trackingDetails");
            TrackingDetail trackingDetail = new TrackingDetail(PackageStatus.valueOf(s.getString("status")),
                                                               s.getLong("dateTime"),
                                                               s.getString("source"),
                                                               s.getString("city"),
                                                               s.getString("state"), s.getString("country"), s.getString("zip"));
            TrackingDetailDTO dto = trackerMapper.convertToDto(trackingDetail, id);
            tdList.add(dto);
        }
        return tdList;
    }
}
