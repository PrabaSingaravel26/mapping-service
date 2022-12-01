package com.census.migration.helper;

import com.census.migration.model.EHRData;
import com.census.migration.model.MappingTable;
import com.census.migration.model.SourceData;
import com.census.migration.model.TargetData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "PatientDetails";

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

    public static List<String> getSheetNames(InputStream inputStream){
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            int sheetCount = workbook.getNumberOfSheets();
            List<String> sheetNames = new ArrayList<>();
            for(int i=1; i<=sheetCount;i++){
                sheetNames.add(workbook.getSheetName(i));
            }
            return sheetNames;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
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
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<EHRData> sourceDataList = new ArrayList<>();
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
                    continue;
                }
                for(int index = 0; index < headerNames.size(); index++){
                    Cell currentCell = cellsInRow.next();
                    CellType cellType =currentCell.getCellType();
                    if(CellType.STRING == cellType){
                        dataMap.put(headerNames.get(index), currentCell.getStringCellValue());
                    }else if(CellType.NUMERIC == cellType){
                        dataMap.put(headerNames.get(index), currentCell.getNumericCellValue());
                    }else if(CellType.BOOLEAN == cellType){
                        dataMap.put(headerNames.get(index), currentCell.getBooleanCellValue());
                    }
                }
                ehrData.setData(dataMap);
                sourceDataList.add(ehrData);
            }
            workbook.close();
            return sourceDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static List<TargetData> sourceToTargetData(InputStream inputStream, MappingTable mappingTable) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<TargetData> targetDataList = new ArrayList<>();
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
                    continue;
                }
                for(int index = 0; index < headerNames.size(); index++){
                    Cell currentCell = cellsInRow.next();
                    CellType cellType =currentCell.getCellType();
                    String headerName = headerNames.get(index);
                    String targetColumnName = mappingTable.getSourceTargetColumnMap().get(headerName);
                    if(CellType.STRING == cellType){
                        dataMap.put(targetColumnName, currentCell.getStringCellValue());
                    }else if(CellType.NUMERIC == cellType){
                        dataMap.put(targetColumnName, currentCell.getNumericCellValue());
                    }else if(CellType.BOOLEAN == cellType){
                        dataMap.put(targetColumnName, currentCell.getBooleanCellValue());
                    }
                }
                targetData.setData(dataMap);
                targetDataList.add(targetData);
            }
            workbook.close();
            return targetDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
