package com.census.migration.repository;

import com.census.migration.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {

    Branch findBySourceEHRAndDatasetValue(String sourceEHR, String datasetValue);
}
