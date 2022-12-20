package com.census.migration.repository;

import com.census.migration.model.EHRData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EHRDataRepository extends JpaRepository<EHRData, Integer> {
}
