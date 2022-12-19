package com.census.migration.controller;

import com.census.migration.dto.MappingResponseDto;
import com.census.migration.model.MappingData;
import com.census.migration.model.TargetData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/migration")
public interface MappingApi {
    @PostMapping("/mapping/sourceEHR/{sourceEHR}/targetEHR/{targetEHR}")
    String saveMappingFields(@PathVariable("sourceEHR") String sourceEHRType,
                             @PathVariable("targetEHR") String targetEHRType,
                             @RequestParam("mappingFile") MultipartFile mappingFile);

    @GetMapping("/mapping")
    MappingData getMappingFields(@RequestParam("sourceEHRType") String sourceEHRType);

    @PostMapping("/TransformData")
    List<TargetData> saveTargetData(@RequestParam("file") MultipartFile file);

    @GetMapping("/mapping/sourceEHRType/{sourceEHRType}/targetEHRType/{targetEHRType}/details")
    MappingResponseDto getMappingDetails(@PathVariable("sourceEHRType") String sourceEHRType, @PathVariable("targetEHRType") String targetEHRType);

}
