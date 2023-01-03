package com.census.migration.service;

import com.census.migration.model.MappingData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface MappingService {

    String saveMappingFields(String sourceEHR, String targetEHR, MultipartFile mappingFile);

    MappingData getMappingFields(String sourceEHRType);

    String saveEHRMapping(String sourceEHR, String targetEHR, MultipartFile mappingFile);

}
