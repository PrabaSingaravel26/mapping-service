package com.census.migration.repository;

import com.census.migration.model.MappingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MappingDataRepository extends JpaRepository<MappingData, String> {
    MappingData findBySourceEHR(String sourceEHRType);

    List<MappingData> findBySourceEHRAndTargetEHR(String sourceEHRType, String targetEHR);
}
