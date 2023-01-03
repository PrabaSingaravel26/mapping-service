package com.census.migration.repository;

import com.census.migration.model.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddBatchDataRepository extends JpaRepository<BatchData,Integer> {

    List<BatchData> getByClientId(int clientId);
}
