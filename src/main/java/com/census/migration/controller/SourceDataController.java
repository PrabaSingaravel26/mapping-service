package com.census.migration.controller;

import com.census.migration.model.EHRData;
import com.census.migration.service.SourceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class SourceDataController implements SourceDataApi {

    @Autowired
    private SourceDataService sourceDataService;

    public String uploadSourceFile(MultipartFile file) {
        return sourceDataService.saveSourceData(file);
    }

    @Override
    public List<String> getHeaderNames(MultipartFile file) {
        return sourceDataService.getHeaderNames(file);
    }

    @Override
    public String uploadEHRFile(MultipartFile file) {
        return sourceDataService.saveEHRData(file);
    }

    @Override
    public List<EHRData> getEHRDate() {
        return sourceDataService.getEHRDate();
    }

}
