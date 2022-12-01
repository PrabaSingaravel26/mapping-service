package com.census.migration.controller;

import com.census.migration.model.MappingTable;
import com.census.migration.model.TargetData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/migration")
public interface MappingApi {
    @PostMapping("/mapping")
    String saveMappingFields(@RequestBody MappingTable mappingTable);

    @GetMapping("/mapping")
    MappingTable getMappingFields(@RequestParam("sourceEHRType") String sourceEHRType);

    @PostMapping("/TransformData")
    List<TargetData> saveTargetData(@RequestParam("file") MultipartFile file);
}
