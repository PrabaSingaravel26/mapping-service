package com.census.migration.controller;

import com.census.migration.dto.MappingResponseDto;
import com.census.migration.model.TargetData;
import com.census.migration.service.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
public class TransformationController implements TransformationApi{

    @Autowired
    private TransformationService transformationService;

    @Override
    public List<TargetData> saveTargetData(MultipartFile file) {
        return transformationService.saveTargetData(file);
    }

    @Override
    public MappingResponseDto getMappingDetails(String sourceEHRType, String targetEHRType) {
        return transformationService.getMappingSourceToTargetMappingDetails(sourceEHRType, targetEHRType);
    }

    @Override
    public String transformSourceToTargetFile(List<Integer> patient_id) throws ParseException {
        return transformationService.transformSourceToTargetFile(patient_id);
    }
}
