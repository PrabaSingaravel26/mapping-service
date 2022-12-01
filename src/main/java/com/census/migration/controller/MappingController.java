package com.census.migration.controller;

import com.census.migration.model.MappingTable;
import com.census.migration.model.TargetData;
import com.census.migration.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class MappingController implements MappingApi {

    @Autowired
    private MappingService mappingService;

    @Override
    public String saveMappingFields(MappingTable mappingTable) {
        return mappingService.saveMappingFields(mappingTable);
    }

    @Override
    public MappingTable getMappingFields(String sourceEHRType) {
        return mappingService.getMappingFields(sourceEHRType);
    }

    @Override
    public List<TargetData> saveTargetData(MultipartFile file) {
        return mappingService.saveTargetData(file);
    }
}
