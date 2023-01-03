package com.census.migration.helper;

import com.census.migration.model.EHRData;
import com.census.migration.model.EHRMapping;
import com.census.migration.model.MappingData;
import com.census.migration.model.SourceEHRFormat;
import com.census.migration.model.TargetData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcelHelper {

    static Map<String,String> branchMap = Map.of(
            "Queen City Hospice", "451 - BRANCH 451",
            "Day City Hospice", "452 - BRANCH 452",
            "Capital City Hospice", "453 - BRANCH 453"
    );

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "Basic Info";
    static String MAPPING_SHEET = "MappingSheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<String> getSheetNames(Workbook workbook){
        int sheetCount = workbook.getNumberOfSheets();
        List<String> sheetNames = new ArrayList<>();
        for(int index = 0; index < sheetCount; index++){
            sheetNames.add(workbook.getSheetName(index));
        }
        return sheetNames;
    }

    public static List<String> getHeaderNames(InputStream inputStream){
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            Row currentRow = rows.next();
            Iterator<Cell> cellsInRow = currentRow.iterator();
            List<String> headerNames = new ArrayList<>();
            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                headerNames.add(currentCell.getStringCellValue());
                cellIdx++;
            }
            workbook.close();
            return headerNames;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<EHRData> excelToEHRData(List<SourceEHRFormat> sourceEHRFormatList, InputStream inputStream) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            List<String> sheetNames = getSheetNames(workbook);
            List<EHRData> sourceDataList = new ArrayList<>();
            for(int sheetIndex = 0; sheetIndex < sheetNames.size(); sheetIndex++){
                String sheetName = sheetNames.get(sheetIndex);
                Sheet sheet = workbook.getSheet(sheetName);
                List<SourceEHRFormat> sourceEHRFormats = sourceEHRFormatList.stream()
                                                                  .filter(c -> c.getSourceSheetName()
                                                                                .equals(sheetName))
                                                                  .collect(Collectors.toList());
                Iterator<Row> rows = sheet.iterator();
                List<String> headerNames = new ArrayList<>();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    Map<String, Object> dataMap = new HashMap<>();
                    EHRData ehrData = new EHRData();
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    int cellIdx = 0;
                    if (rowNumber == 0) {
                        while (cellsInRow.hasNext()) {
                            Cell currentCell = cellsInRow.next();
                            headerNames.add(currentCell.getStringCellValue());
                            cellIdx++;
                        }
                        rowNumber++;
                    }else{
                        for(SourceEHRFormat fieldFormat : sourceEHRFormats){
                            Cell currentCell = cellsInRow.next();
                            if(fieldFormat.getSourceFieldName().equals("Consolo Unique Patient ID")) {
                                ehrData.setSourceId((int) currentCell.getNumericCellValue());
                            }
                            if(fieldFormat.getFieldType().equals("String")) {
                                dataMap.put(fieldFormat.getSourceFieldName(), currentCell.getStringCellValue());
                            } else if(fieldFormat.getFieldType().equals("Integer")){
                                dataMap.put(fieldFormat.getSourceFieldName(), currentCell.getNumericCellValue());
                            } else if(fieldFormat.getFieldType().equals("Date")){
                                if(currentCell.getDateCellValue() != null) {
                                    SimpleDateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    dataMap.put(fieldFormat.getSourceFieldName(),
                                                originalFormat.format(currentCell.getDateCellValue()));
                                }else {
                                    dataMap.put(fieldFormat.getSourceFieldName(),currentCell.getDateCellValue());
                                }
                            }
                        }
                        ehrData.setSheetName(sheetName);
                        ehrData.setData(dataMap);
                        sourceDataList.add(ehrData);
                    }
                }
            }
            workbook.close();
            return sourceDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<TargetData> sourceToTargetData(InputStream inputStream, List<MappingData> mappingTable) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            List<String> sheetNames = getSheetNames(workbook);
            List<TargetData> targetDataList = new ArrayList<>();
            for(int sheetIndex = 0; sheetIndex < sheetNames.size(); sheetIndex++) {
                String sheetName = sheetNames.get(sheetIndex);
                Sheet sheet = workbook.getSheet(sheetName);
                Iterator<Row> rows = sheet.iterator();
                List<String> headerNames = new ArrayList<>();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    Map<String, Object> dataMap = new HashMap<>();
                    TargetData targetData = new TargetData();
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    int cellIdx = 0;
                    if (rowNumber == 0) {
                        while (cellsInRow.hasNext()) {
                            Cell currentCell = cellsInRow.next();
                            headerNames.add(currentCell.getStringCellValue());
                            cellIdx++;
                        }
                        rowNumber++;
                    }else {
                        for (int index = 0; index < headerNames.size(); index++) {
                            Cell currentCell = cellsInRow.next();
                            CellType cellType = currentCell.getCellType();
                            String headerName = headerNames.get(index);
                            List<MappingData> targetColumnNames = mappingTable.stream()
                                                                              .filter(c -> c.getSourceColumnName()
                                                                                            .equals(headerName))
                                                                              .collect(Collectors.toList());
                            if (index == 0) {
                                targetData.setPatientId((int) currentCell.getNumericCellValue());
                            }
                            if (CellType.STRING == cellType) {
                                dataMap.put(targetColumnNames.get(0).getTargetColumnName(),
                                            currentCell.getStringCellValue());
                            } else if (CellType.NUMERIC == cellType) {
                                dataMap.put(targetColumnNames.get(0).getTargetColumnName(),
                                            currentCell.getNumericCellValue());
                            } else if (CellType.BOOLEAN == cellType) {
                                dataMap.put(targetColumnNames.get(0).getTargetColumnName(),
                                            currentCell.getBooleanCellValue());
                            }
                        }
                        List<MappingData> targetSheetNames = mappingTable.stream()
                                                                         .filter(c -> c.getSourceSheetName()
                                                                                       .equals(sheetName))
                                                                         .collect(Collectors.toList());
                        targetData.setTargetSheetName(targetSheetNames.get(0).getTargetSheetName());
                        targetData.setTargetData(dataMap);
                        targetDataList.add(targetData);
                    }
                }
            }
            workbook.close();
            return targetDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static String writeToExcelFile(List<TargetData> targetData) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        for(int sheetIndex = 0; sheetIndex < targetData.size(); sheetIndex++){
            String sheetName = targetData.get(sheetIndex).getTargetSheetName();
            Map<String,Object> data = targetData.get(sheetIndex).getTargetData();
            Set<String> keySet = data.keySet();
            if(workbook.getSheet(sheetName) == null) {
                sheet = workbook.createSheet(sheetName);
                int rowNum = 0;
                Row row = sheet.createRow(rowNum);
                int cellNum = 0;
                for (String key : keySet) {
                    Cell cell = row.createCell(cellNum++);
                    cell.setCellValue(key);
                }
            }
            sheet = workbook.getSheet(sheetName);
            Row row = sheet.createRow(sheet.getLastRowNum()+1);
            int cellNumber = 0;
            for (String key : keySet) {
                Object obj = data.get(key);
                Cell cell = row.createCell(cellNumber++);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Number) {
                    cell.setCellValue((Double) obj);
                } else if(obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                }
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File("patient.xlsx"));
            workbook.write(out);
            out.close();
            return "patient.xlsx written successfully.";
        }
        catch (Exception e) {
            throw new RuntimeException("fail to write Excel file: " + e.getMessage());
        }
    }

    public static List<MappingData> excelToMappingData(String sourceEHR, String targetEHR, InputStream inputStream) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(MAPPING_SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<MappingData> mappingDataList = new ArrayList<>();
            List<String> headerNames = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Map<String, Object> dataMap = new HashMap<>();
                MappingData mappingData = new MappingData();
                mappingData.setSourceEHR(sourceEHR);
                mappingData.setTargetEHR(targetEHR);
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIdx = 0;
                if (rowNumber == 0) {
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        headerNames.add(currentCell.getStringCellValue());
                        cellIdx++;
                    }
                    rowNumber++;
                    continue;
                }
                for(int index = 0; index < headerNames.size(); index++){
                    Cell currentCell = cellsInRow.next();
                    CellType cellType =currentCell.getCellType();
                    String headerName = headerNames.get(index);
                    switch (headerName) {
                        case "sourceSheetName":
                            mappingData.setSourceSheetName(currentCell.getStringCellValue());
                            break;
                        case "sourceColumnName":
                            mappingData.setSourceColumnName(currentCell.getStringCellValue());
                            break;
                        case "targetSheetName":
                            mappingData.setTargetSheetName(currentCell.getStringCellValue());
                            break;
                        case "targetColumnName":
                            mappingData.setTargetColumnName(currentCell.getStringCellValue());
                            break;
                        case "requiredField":
                            mappingData.setRequiredField(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                mappingDataList.add(mappingData);
            }
            workbook.close();
            return mappingDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<TargetData> transformSourceToTargetData(List<EHRData> ehrDataList, List<EHRMapping> mappingTable) {

        List<TargetData> targetDataList = new ArrayList<>();

        Map<String, List<EHRMapping>> mappingDataPerSheetName = mappingTable.stream()
                                                              .collect(Collectors.groupingBy(EHRMapping::getTargetSheetName));
        Set<String> targetSheetSet = mappingDataPerSheetName.keySet();
        for(String targetSheet : targetSheetSet){
            TargetData targetData = new TargetData();
            Map<String, Object> dataMap = new HashMap<>();
            List<EHRMapping> mappingDataList = mappingDataPerSheetName.get(targetSheet);
            for(EHRMapping mappingData : mappingDataList){
                EHRData sourceEhrData = ehrDataList.stream().filter(c -> c.getSheetName().equals(mappingData.getSourceSheetName())).findFirst().orElse(null);
                targetData.setTargetFileName(mappingData.getTargetFileName());
                targetData.setTargetSheetName(mappingData.getTargetSheetName());
                if(sourceEhrData != null) {
                    targetData.setPatientId(sourceEhrData.getSourceId());
                    switch (mappingData.getTargetFieldType()){
                        case "SelectMap":
                            if(mappingData.getTargetFieldFormat().split(":")[1].equals("Branch")){
                                String sourceBranch = sourceEhrData.getData().get(mappingData.getSourceFieldName()).toString();
                                String hchbValue = branchMap.get(sourceBranch);
                                dataMap.put(mappingData.getTargetFieldName(),
                                            hchbValue);
                            }
                            break;
                        case "Default":
                            dataMap.put(mappingData.getTargetFieldName(),
                                                   mappingData.getTargetFieldFormat());
                            break;
                        case "Split":
                            String regex = mappingData.getTargetFieldFormat().split(":")[0];
                            Integer index = Integer.parseInt(mappingData.getTargetFieldFormat().split(":")[1]);
                            dataMap.put(mappingData.getTargetFieldName(),
                                        sourceEhrData.getData().get(mappingData.getSourceFieldName()).toString()
                                                     .split(regex)[index]);
                            break;
                        default:
                            dataMap.put(mappingData.getTargetFieldName(),
                                        sourceEhrData.getData().get(mappingData.getSourceFieldName()));
                            break;
                    }
                }else {
                    dataMap.put(mappingData.getTargetFieldName(),null);
                }
            }
            targetData.setTargetData(dataMap);
            targetDataList.add(targetData);
        }
        return targetDataList;
    }

    public static List<EHRMapping> excelToEHRMapping(String sourceEHR, String targetEHR, InputStream inputStream) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            List<String> sheetNames = getSheetNames(workbook);
            Sheet sheet = workbook.getSheet(sheetNames.get(0));
            Iterator<Row> rows = sheet.iterator();
            List<EHRMapping> mappingDataList = new ArrayList<>();
            List<String> headerNames = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Map<String, Object> dataMap = new HashMap<>();
                EHRMapping ehrMapping = new EHRMapping();
                ehrMapping.setSourceEHR(sourceEHR);
                ehrMapping.setTargetEHR(targetEHR);
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIdx = 0;
                if (rowNumber == 0) {
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        headerNames.add(currentCell.getStringCellValue());
                        cellIdx++;
                    }
                    rowNumber++;
                    continue;
                }
                Map<String,String> inputMap = new HashMap<>();
                for(int index = 0; index < headerNames.size(); index++){
                    Cell currentCell = cellsInRow.next();
                    CellType cellType =currentCell.getCellType();
                    inputMap.put(headerNames.get(index),currentCell.getStringCellValue());
                    cellIdx++;
                }
                ehrMapping.setSourceFileName(inputMap.get("SRC File Name"));
                ehrMapping.setSourceSheetName(inputMap.get("SRC Sheet Name"));
                ehrMapping.setSourceFieldName(inputMap.get("SRC Field Name"));
                ehrMapping.setMapping(inputMap.get("Mapping"));
                ehrMapping.setSourceFieldType(inputMap.get("SRC Field Type"));
                ehrMapping.setSourceFieldFormat(inputMap.get("SRC Field Format"));
                ehrMapping.setTargetFileName(inputMap.get("Target File Name"));
                ehrMapping.setTargetSheetName(inputMap.get("Target Sheet Name"));
                ehrMapping.setTargetFieldName(inputMap.get("Target Field Name"));
                ehrMapping.setTargetFieldMandatory(inputMap.get("Target Field Mandatory"));
                ehrMapping.setTargetFieldType(inputMap.get("Target Field Type"));
                ehrMapping.setTargetFieldFormat(inputMap.get("Target Field Format"));
                mappingDataList.add(ehrMapping);
            }
            workbook.close();
            return mappingDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}
