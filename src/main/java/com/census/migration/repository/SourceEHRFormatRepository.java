package com.census.migration.repository;

import com.census.migration.model.SourceEHRFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceEHRFormatRepository extends JpaRepository<SourceEHRFormat,Integer> {
    List<SourceEHRFormat> getBySourceEHR(String sourceEHR);
}
