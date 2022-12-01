package com.census.migration.repository;

import com.census.migration.model.TargetData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TargetDataRepository extends JpaRepository<TargetData, UUID> {
}
