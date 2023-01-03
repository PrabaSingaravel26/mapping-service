package com.census.migration.controller;

import com.census.migration.model.MappingData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/migration")
public interface MappingApi {
    @PostMapping("/mapping/sourceEHR/{sourceEHR}/targetEHR/{targetEHR}")
    String saveMappingFields(@PathVariable("sourceEHR") String sourceEHRType,
                             @PathVariable("targetEHR") String targetEHRType,
                             @RequestParam("mappingFile") MultipartFile mappingFile);

    @GetMapping("/mapping")
    MappingData getMappingFields(@RequestParam("sourceEHRType") String sourceEHRType);

    @PostMapping("/ehr_mapping/sourceEHR/{sourceEHR}/targetEHR/{targetEHR}")
    String saveEHRMapping(@PathVariable("sourceEHR") String sourceEHRType,
                             @PathVariable("targetEHR") String targetEHRType,
                             @RequestParam("mappingFile") MultipartFile mappingFile);

}
