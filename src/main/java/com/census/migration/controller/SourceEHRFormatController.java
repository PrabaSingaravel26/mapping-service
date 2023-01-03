package com.census.migration.controller;

import com.census.migration.model.SourceEHRFormat;
import com.census.migration.service.SourceEHRFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SourceEHRFormatController implements SourceEHRFormatApi{

    @Autowired
    private SourceEHRFormatService sourceEHRFormatService;

    @Override
    public List<SourceEHRFormat> saveSourceEHRFormat(List<SourceEHRFormat> sourceEHRFormat) {
        return sourceEHRFormatService.saveSourceEHRFormat(sourceEHRFormat);
    }

    @Override
    public List<SourceEHRFormat> getSourceEHRFormat(String sourceEHR) {
        return sourceEHRFormatService.getSourceEHRFormat(sourceEHR);
    }
}
