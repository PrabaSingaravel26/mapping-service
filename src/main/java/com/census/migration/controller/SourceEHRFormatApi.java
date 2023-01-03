package com.census.migration.controller;

import com.census.migration.model.BatchData;
import com.census.migration.model.SourceEHRFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/migration")
public interface SourceEHRFormatApi {

    @PostMapping("/source_ehr_format")
    List<SourceEHRFormat> saveSourceEHRFormat(@RequestBody List<SourceEHRFormat> sourceEHRFormat);

    @GetMapping("/source_ehr_format")
    List<SourceEHRFormat> getSourceEHRFormat(@RequestParam("sourceEHR") String sourceEHR);
}
