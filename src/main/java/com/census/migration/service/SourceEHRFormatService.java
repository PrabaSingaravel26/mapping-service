package com.census.migration.service;

import com.census.migration.model.SourceEHRFormat;

import java.util.List;

public interface SourceEHRFormatService {
    List<SourceEHRFormat> saveSourceEHRFormat(List<SourceEHRFormat> sourceEHRFormat);

    List<SourceEHRFormat> getSourceEHRFormat(String sourceEHR);
}
