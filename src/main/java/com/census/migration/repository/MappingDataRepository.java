package com.census.migration.repository;

import com.census.migration.model.MappingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MappingDataRepository extends JpaRepository<MappingTable, String> {
    MappingTable findBySourceEHR(String sourceEHRType);
}
