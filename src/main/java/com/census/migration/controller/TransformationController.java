package com.census.migration.controller;

import com.census.migration.service.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TransformationController implements TransformationApi{

    @Autowired
    private TransformationService transformationService;

    @Override
    public String transformSourceToTargetFile(int patient_id) {
        return transformationService.transformSourceToTargetFile(patient_id);
    }
}
