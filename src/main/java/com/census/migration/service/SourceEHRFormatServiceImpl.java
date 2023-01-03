package com.census.migration.service;

import com.census.migration.model.SourceEHRFormat;
import com.census.migration.repository.SourceEHRFormatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceEHRFormatServiceImpl implements SourceEHRFormatService {

    @Autowired
    private SourceEHRFormatRepository sourceEHRFormatRepository;

    @Override
    public List<SourceEHRFormat> saveSourceEHRFormat(List<SourceEHRFormat> sourceEHRFormat) {
        return sourceEHRFormatRepository.saveAll(sourceEHRFormat);
    }

    @Override
    public List<SourceEHRFormat> getSourceEHRFormat(String sourceEHR) {
        return sourceEHRFormatRepository.getBySourceEHR(sourceEHR);
    }
}
