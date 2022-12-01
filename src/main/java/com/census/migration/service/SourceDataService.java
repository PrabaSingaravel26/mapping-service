package com.census.migration.service;

import com.census.migration.model.EHRData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SourceDataService {
    String saveSourceData(MultipartFile file);

    List<String> getHeaderNames(MultipartFile file);

    String saveEHRData(MultipartFile file);

    List<EHRData> getEHRDate();
}
