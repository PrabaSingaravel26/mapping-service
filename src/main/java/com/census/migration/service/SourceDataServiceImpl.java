package com.census.migration.service;

import com.census.migration.helper.CSVHelper;
import com.census.migration.helper.ExcelHelper;
import com.census.migration.model.EHRData;
import com.census.migration.model.SourceData;
import com.census.migration.repository.EHRDataRepository;
import com.census.migration.repository.SourceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class SourceDataServiceImpl implements SourceDataService {

    @Autowired
    private SourceDataRepository sourceDataRepository;

    @Autowired
    private EHRDataRepository ehrDataRepository;

    @Override
    public String saveSourceData(MultipartFile file) {
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                List<SourceData> sourceDataList = CSVHelper.csvToSourceData(file.getInputStream());
                sourceDataRepository.saveAll(sourceDataList);
                return "Uploaded the csv file successfully: " + file.getOriginalFilename();
            } catch (IOException e) {
                throw new RuntimeException("fail to store csv data: " + e.getMessage());
            }
        } else if(ExcelHelper.hasExcelFormat(file)){
            try {
                List<SourceData> sourceDataList = ExcelHelper.excelToSourceData(file.getInputStream());
                sourceDataRepository.saveAll(sourceDataList);
                return "Uploaded the excel file successfully: " + file.getOriginalFilename();
            } catch (IOException e) {
                throw new RuntimeException("fail to store excel data: " + e.getMessage());
            }
        } else {
            return "Please upload a csv/excel file!";
        }
    }

    @Override
    public List<String> getHeaderNames(MultipartFile file) {
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                return CSVHelper.getHeaderList(file.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException("fail to get header from csv file: " + e.getMessage());
            }
        } else if(ExcelHelper.hasExcelFormat(file)){
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
    public String saveEHRData(MultipartFile file) {
        if(ExcelHelper.hasExcelFormat(file)){
            try {
                List<EHRData> sourceDataList = ExcelHelper.excelToEHRData(file.getInputStream());
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
