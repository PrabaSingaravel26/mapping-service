package com.census.migration.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/migration")
public interface TransformationApi {
    @PostMapping("/Transformation")
    String transformSourceToTargetFile(@RequestParam("patient_id") List<Integer> patient_id);
}
