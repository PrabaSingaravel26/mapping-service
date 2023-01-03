package com.census.migration.controller;

import com.census.migration.dto.MappingResponseDto;
import com.census.migration.model.TargetData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/migration")
public interface TransformationApi {
    @PostMapping("/TransformData")
    List<TargetData> saveTargetData(@RequestParam("file") MultipartFile file);

    @GetMapping("/mapping/sourceEHRType/{sourceEHRType}/targetEHRType/{targetEHRType}/details")
    MappingResponseDto getMappingDetails(@PathVariable("sourceEHRType") String sourceEHRType, @PathVariable("targetEHRType") String targetEHRType);

    @PostMapping("/Transformation")
    String transformSourceToTargetFile(@RequestParam("patient_id") List<Integer> patient_id) throws ParseException;
}
