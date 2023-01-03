package com.census.migration.service;

import com.census.migration.helper.ExcelHelper;
import com.census.migration.model.EHRData;
import com.census.migration.model.SourceEHRFormat;
import com.census.migration.repository.EHRDataRepository;
import com.census.migration.repository.SourceEHRFormatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class SourceDataServiceImpl implements SourceDataService {

    @Autowired
    private EHRDataRepository ehrDataRepository;

    @Autowired
    private SourceEHRFormatRepository sourceEHRFormatRepository;

    @Override
    public List<String> getHeaderNames(MultipartFile file) {
        if(ExcelHelper.hasExcelFormat(file)){
            try {
                return ExcelHelper.getHeaderNames(file.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException("fail to get header from excel file: " + e.getMessage());
            }
        } else {
            return Collections.singletonList("Please upload a csv/excel file!");
        }
    }

    @Override
    public String saveEHRData(String sourceEHR, MultipartFile file) {
        if(ExcelHelper.hasExcelFormat(file)){
            try {
                List<SourceEHRFormat> sourceEHRFormatList = sourceEHRFormatRepository.getBySourceEHR(sourceEHR);
                List<EHRData> sourceDataList = ExcelHelper.excelToEHRData(sourceEHRFormatList, file.getInputStream());
                ehrDataRepository.saveAll(sourceDataList);
                return "Uploaded the excel file successfully: " + file.getOriginalFilename();
            } catch (IOException e) {
                throw new RuntimeException("fail to store excel data: " + e.getMessage());
            }
        } else {
            return "Please upload a csv/excel file!";
        }
    }

    @Override
    public List<EHRData> getEHRDate() {
        return ehrDataRepository.findAll();
    }
}
