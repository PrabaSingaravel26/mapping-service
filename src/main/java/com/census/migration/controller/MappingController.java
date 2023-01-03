package com.census.migration.controller;

import com.census.migration.model.MappingData;
import com.census.migration.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MappingController implements MappingApi {

    @Autowired
    private MappingService mappingService;

    @Override
    public String saveMappingFields(String sourceEHR, String targetEHR, MultipartFile mappingFile) {
        return mappingService.saveMappingFields(sourceEHR,targetEHR,mappingFile);
    }

    @Override
    public MappingData getMappingFields(String sourceEHRType) {
        return mappingService.getMappingFields(sourceEHRType);
    }

    @Override
    public String saveEHRMapping(String sourceEHRType, String targetEHRType, MultipartFile mappingFile) {
        return mappingService.saveEHRMapping(sourceEHRType,targetEHRType,mappingFile);
    }

}
