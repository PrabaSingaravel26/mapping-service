package com.census.migration.controller;

import com.census.migration.model.EHRData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/migration")
public interface SourceDataApi {

    @GetMapping("/header")
    List<String> getHeaderNames(@RequestParam("file") MultipartFile file);

    @PostMapping("/uploadEHR")
    String uploadEHRFile(@RequestParam("sourceEHR") String sourceEHR, @RequestParam("file") MultipartFile file);

    @GetMapping("/EHRData")
    List<EHRData> getEHRDate();
}
