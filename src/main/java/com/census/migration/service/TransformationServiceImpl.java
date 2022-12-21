package com.census.migration.service;

import com.census.migration.helper.ExcelHelper;
import com.census.migration.model.TargetData;
import com.census.migration.repository.TargetDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransformationServiceImpl implements TransformationService {

    @Autowired
    private TargetDataRepository targetDataRepository;

    @Override
    public String transformSourceToTargetFile(List<Integer> patient_id) {
        List<TargetData> targetData = targetDataRepository.findByTargetId(patient_id);
        return ExcelHelper.writeToExcelFile(targetData);
    }
}
