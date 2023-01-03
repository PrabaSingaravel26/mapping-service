package com.census.migration.service;

import com.census.migration.dto.MappingResponseDto;
import com.census.migration.model.TargetData;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface TransformationService {

    List<TargetData> saveTargetData(MultipartFile file);

    MappingResponseDto getMappingSourceToTargetMappingDetails(String sourceEHRType, String targetEHRType);

    String transformSourceToTargetFile(List<Integer> patient_id) throws ParseException;
}
