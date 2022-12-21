package com.census.migration.helper;

import com.census.migration.model.EHRData;
import com.census.migration.model.MappingData;
import com.census.migration.model.SourceData;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "Basic Info";
    static String MAPPING_SHEET = "MappingSheet";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<SourceData> excelToSourceData(InputStream inputStream) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<SourceData> sourceDataList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                SourceData sourceData = new SourceData();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            sourceData.setPatientId((int) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            sourceData.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            sourceData.setAddress(currentCell.getStringCellValue());
                            break;
                        case 3:
                            sourceData.setState(currentCell.getStringCellValue());
                            break;
                        case 4:
                            sourceData.setZipCode((int) currentCell.getNumericCellValue());
                            break;
                        case 5:
                            sourceData.setCounty(currentCell.getStringCellValue());
                            break;
                        case 6:
                            sourceData.setMobileNumber((long) currentCell.getNumericCellValue());
                            break;
                        case 7:
                            sourceData.setGender(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                sourceDataList.add(sourceData);
            }
            workbook.close();
            return sourceDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
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

    public static List<EHRData> excelToEHRData(InputStream inputStream) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            List<String> sheetNames = getSheetNames(workbook);
            List<EHRData> sourceDataList = new ArrayList<>();
            for(int sheetIndex = 0; sheetIndex < sheetNames.size(); sheetIndex++){
                Sheet sheet = workbook.getSheet(sheetNames.get(sheetIndex));
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
                        for(int index = 0; index < headerNames.size(); index++){
                            Cell currentCell = cellsInRow.next();
                            if(index == 0) {
                                ehrData.setSourceId((int) currentCell.getNumericCellValue());
                            }
                            CellType cellType =currentCell.getCellType();
                            if(CellType.STRING == cellType){
                                dataMap.put(headerNames.get(index), currentCell.getStringCellValue());
                            }else if(CellType.NUMERIC == cellType){
                                dataMap.put(headerNames.get(index), currentCell.getNumericCellValue());
                            }else if(CellType.BOOLEAN == cellType){
                                dataMap.put(headerNames.get(index), currentCell.getBooleanCellValue());
                            }
                        }
                        ehrData.setSheetName(sheetNames.get(sheetIndex));
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
                                targetData.setTargetId((int) currentCell.getNumericCellValue());
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
                        targetData.setSheetName(targetSheetNames.get(0).getTargetSheetName());
                        targetData.setData(dataMap);
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

    public static String writeToExcelFile(List<TargetData> targetData){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        for(int sheetIndex = 0; sheetIndex < targetData.size(); sheetIndex++){
            String sheetName = targetData.get(sheetIndex).getSheetName();
            Map<String,Object> data = targetData.get(sheetIndex).getData();
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
                else if(obj instanceof Number)
                    cell.setCellValue((Double) obj);
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
}
