package com.census.migration.service;

import com.census.migration.dto.MappingResponseColumnsDto;
import com.census.migration.dto.MappingResponseDto;
import com.census.migration.helper.ExcelHelper;
import com.census.migration.model.EHRData;
import com.census.migration.model.EHRMapping;
import com.census.migration.model.MappingData;
import com.census.migration.model.TargetData;
import com.census.migration.repository.EHRDataRepository;
import com.census.migration.repository.EHRMappingRepository;
import com.census.migration.repository.MappingDataRepository;
import com.census.migration.repository.TargetDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransformationServiceImpl implements TransformationService {

    @Autowired
    private EHRMappingRepository ehrMappingRepository;

    @Autowired
    private MappingDataRepository mappingDataRepository;

    @Autowired
    private TargetDataRepository targetDataRepository;

    @Autowired
    private EHRDataRepository ehrDataRepository;

    @Override
    public List<TargetData> saveTargetData(MultipartFile file) {
        if(ExcelHelper.hasExcelFormat(file)){
            List<EHRData> ehrDataList = ehrDataRepository.findAll();
            List<EHRMapping> mappingTable = ehrMappingRepository.findBySourceEHRAndTargetEHR("Wellsky", "HCHB");
            Map<Integer, List<EHRData>> ehrDataMap = ehrDataList.stream().collect(
                    Collectors.groupingBy(EHRData::getSourceId, Collectors.toList()));

            for (Map.Entry<Integer, List<EHRData>> entry : ehrDataMap.entrySet()) {
                List<TargetData> targetDataList = ExcelHelper.transformSourceToTargetData(entry.getValue(), mappingTable);
                targetDataList.forEach(s -> s.setPatientId(entry.getKey()));
                targetDataRepository.saveAll(targetDataList);
            }
        }
        return targetDataRepository.findAll();
    }

    @Override
    public MappingResponseDto getMappingSourceToTargetMappingDetails(String sourceEHRType, String targetEHRType) {
        List<MappingData> mappingResponse = mappingDataRepository.findBySourceEHRAndTargetEHR(sourceEHRType, targetEHRType);
        mappingResponse.forEach(System.out::println);
        MappingResponseDto response = new MappingResponseDto();
        response.setSourceEHRType(sourceEHRType);
        response.setDestinationEHRType(targetEHRType);
        List<MappingResponseColumnsDto> columnsDtos = new ArrayList<>();
        mappingResponse.stream().forEach(s -> {
            MappingResponseColumnsDto responseColumnsDto = new MappingResponseColumnsDto();
            responseColumnsDto.setSourceSheetName(s.getSourceSheetName());
            responseColumnsDto.setSourceEHRColumn(s.getSourceColumnName());
            responseColumnsDto.setDestinationSheetName(s.getTargetSheetName());
            responseColumnsDto.setDestinationEHRColumn(s.getTargetColumnName());
            responseColumnsDto.setRequiredField(s.getRequiredField());
            columnsDtos.add(responseColumnsDto);
        });
        response.setMapping(columnsDtos);
        return response;
    }

    @Override
    public String transformSourceToTargetFile(List<Integer> patient_id) throws ParseException {
        List<TargetData> targetData = targetDataRepository.findByPatientId(patient_id);
        return ExcelHelper.writeToExcelFile(targetData);
    }
}
