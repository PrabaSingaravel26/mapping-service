package com.census.migration.service;

import com.census.migration.helper.ExcelHelper;
import com.census.migration.model.TargetData;
import com.census.migration.repository.TargetDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransformationServiceImpl implements TransformationService {

    @Autowired
    private TargetDataRepository targetDataRepository;

    @Override
    public String transformSourceToTargetFile(int patient_id) {
        TargetData targetData = targetDataRepository.getById(patient_id);
        return ExcelHelper.writeToExcelFile(targetData);
    }
}
