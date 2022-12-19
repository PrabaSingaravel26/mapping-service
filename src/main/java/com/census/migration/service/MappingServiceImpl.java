package com.census.migration.service;

import com.census.migration.helper.ExcelHelper;
import com.census.migration.model.MappingData;
import com.census.migration.model.TargetData;
import com.census.migration.repository.MappingDataRepository;
import com.census.migration.repository.TargetDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MappingServiceImpl implements MappingService {

    @Autowired
    private MappingDataRepository mappingDataRepository;

    @Autowired
    private TargetDataRepository targetDataRepository;

    @Override
    public String saveMappingFields(String sourceEHR, String targetEHR, MultipartFile mappingFile) {
        if(ExcelHelper.hasExcelFormat(mappingFile)){
            try {
                List<MappingData> mappingDataList = ExcelHelper.excelToMappingData(sourceEHR,targetEHR,mappingFile.getInputStream());
                mappingDataRepository.saveAll(mappingDataList);
                return "Saved Successfully";
            } catch (IOException e) {
                throw new RuntimeException("fail to store excel data: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public MappingData getMappingFields(String sourceEHRType) {
        return mappingDataRepository.findBySourceEHR(sourceEHRType);
    }

    @Override
    public List<TargetData> saveTargetData(MultipartFile file) {
        if(ExcelHelper.hasExcelFormat(file)){
            try {
                MappingData mappingTable = mappingDataRepository.findBySourceEHR("Wellsky");
                List<TargetData> targetDataList = ExcelHelper.sourceToTargetData(file.getInputStream(), mappingTable);
                return targetDataRepository.saveAll(targetDataList);
            } catch (IOException e) {
                throw new RuntimeException("fail to store excel data: " + e.getMessage());
            }
        }
        return null;
    }
}
