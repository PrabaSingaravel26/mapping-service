package com.census.migration.repository;

import com.census.migration.model.EHRMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EHRMappingRepository extends JpaRepository<EHRMapping, Integer> {
    List<EHRMapping> findBySourceEHRAndTargetEHR(String sourceEHRType, String targetEHR);
}
