package com.census.migration.service;

import com.census.migration.model.MappingData;
import com.census.migration.model.TargetData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MappingService {

    String saveMappingFields(String sourceEHR, String targetEHR, MultipartFile mappingFile);

    MappingData getMappingFields(String sourceEHRType);

    List<TargetData> saveTargetData(MultipartFile file);
}
