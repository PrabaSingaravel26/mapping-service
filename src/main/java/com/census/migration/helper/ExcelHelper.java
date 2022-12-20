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
                ehrData.setSheetName(SHEET);
                ehrData.setData(dataMap);
                sourceDataList.add(ehrData);
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
                    List<MappingData> targetColumnNames = mappingTable.stream()
                                                     .filter(c -> c.getSourceColumnName().equals(headerName))
                                                     .collect(Collectors.toList());
                    if(CellType.STRING == cellType){
                        dataMap.put(targetColumnNames.get(0).getTargetColumnName(), currentCell.getStringCellValue());
                    }else if(CellType.NUMERIC == cellType){
                        dataMap.put(targetColumnNames.get(0).getTargetColumnName(), currentCell.getNumericCellValue());
                    }else if(CellType.BOOLEAN == cellType){
                        dataMap.put(targetColumnNames.get(0).getTargetColumnName(), currentCell.getBooleanCellValue());
                    }
                }
                targetData.setSheetName(mappingTable.get(0).getTargetSheetName());
                targetData.setData(dataMap);
                targetDataList.add(targetData);
            }
            workbook.close();
            return targetDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static String writeToExcelFile(TargetData targetData){
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet(targetData.getSheetName());

        Map<String,Object> data = targetData.getData();
        Set<String> keyset = data.keySet();
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        int cellnum = 0;
        for (String key : keyset) {
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(key);
        }
        row = sheet.createRow(rownum++);
        int cellnumber = 0;
        for (String key : keyset) {
            Object obj = data.get(key);
            Cell cell = row.createCell(cellnumber++);
            if(obj instanceof String)
                cell.setCellValue((String)obj);
            else if(obj instanceof Number)
                cell.setCellValue((Double) obj);
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
