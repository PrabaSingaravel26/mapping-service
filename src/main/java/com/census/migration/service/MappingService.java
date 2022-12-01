package com.census.migration.service;

import com.census.migration.model.MappingTable;
import com.census.migration.model.TargetData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MappingService {

    String saveMappingFields(MappingTable mappingTable);

    MappingTable getMappingFields(String sourceEHRType);

    List<TargetData> saveTargetData(MultipartFile file);
}
