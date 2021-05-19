package com.logistics.packagetracker.serviceimpl;

import com.google.common.base.Strings;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityException;
import com.logistics.packagetracker.repository.TrackerDetailsRepository;
import com.logistics.packagetracker.service.TrackerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TrackerDetailsServiceImpl implements TrackerDetailsService
{
    @Autowired
    TrackerDetailsRepository trackerDetailsRepository;
    
    
    @Override
    public List<TrackingDetails> findAll()
    {
        return trackerDetailsRepository.findAll();
    }
    
    @Override
    public TrackingDetails findById(String id)
    {
        if (Strings.isNullOrEmpty(id))
        {
            throw new EntityException("Provide a valid package tracker ID");
        }
        return trackerDetailsRepository.findById(id).orElseThrow(() -> new EntityException("Provide a valid package tracker ID"));
    }
    
    @Override
    public TrackingDetails save(TrackingDetails entity)
    {
        if (Objects.isNull(entity) || Strings.isNullOrEmpty(entity.getId()))
        {
            throw new EntityException("Provide valid tracker detail");
        }
        return trackerDetailsRepository.save(entity);
    }
    
    @Override
    public TrackingDetails update(TrackingDetails entity)
    {
        if (Objects.isNull(entity) || Strings.isNullOrEmpty(entity.getId()))
        {
            throw new EntityException("Provide valid tracker details");
        }
        return trackerDetailsRepository.save(entity);
    }
    
    @Override
    public boolean existsById(String var1)
    {
        return trackerDetailsRepository.existsById(var1);
    }
    
    @Override
    public long count()
    {
        return trackerDetailsRepository.count();
    }
    
    @Override
    public TrackingDetails findByTrackingCode(String code, PackageStatus status)
    {
        return trackerDetailsRepository.findByTrackingCode(code, status);
    }
    
    @Override
    public TrackingDetails findByStatus(PackageStatus status)
    {
        return trackerDetailsRepository.findByStatus(status);
    }
}
