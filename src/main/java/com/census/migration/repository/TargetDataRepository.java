package com.census.migration.repository;

import com.census.migration.model.TargetData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetDataRepository extends JpaRepository<TargetData, Integer> {

    @Query(value = "select * from target_data where patient_id in :patientIds", nativeQuery = true)
    List<TargetData> findByPatientId(@Param("patientIds")  List<Integer> patientIds);
}
