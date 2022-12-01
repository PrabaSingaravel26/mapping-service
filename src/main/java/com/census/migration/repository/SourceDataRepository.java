package com.census.migration.repository;

import com.census.migration.model.SourceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceDataRepository extends JpaRepository<SourceData, Integer> {
}
