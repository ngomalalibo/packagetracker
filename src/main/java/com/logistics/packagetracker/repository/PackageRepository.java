package com.logistics.packagetracker.repository;

import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.enumeration.PackageStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends MongoRepository<Package, String>
{
    @Query("{'status':?0}")
    Optional<List<Package>> findByStatus(String status, Sort sort);
}
