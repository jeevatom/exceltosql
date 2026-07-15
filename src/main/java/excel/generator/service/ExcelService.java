package excel.generator.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import excel.generator.model.ColumnInfo;

@Service
public class ExcelService {

    public List<ColumnInfo> getColumns(MultipartFile file) throws EncryptedDocumentException, IOException {
   
            List<ColumnInfo> columns = new ArrayList<>();

            Workbook workbook = WorkbookFactory.create(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);

            DataFormatter formatter = new DataFormatter();

            List<String> headers = new ArrayList<>();

            int lastColumn = headerRow.getLastCellNum();

            for (int i = 0; i < lastColumn; i++) {
                Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                String columnData = "";
                if (cell != null) {
                    columnData = formatter.formatCellValue(cell);
                }

                if (columnData.trim().isEmpty() || columnData.trim() == "" || columnData.trim() == null) {
                    columnData = "empty " + i;
                }
                String dbName = convertToDatabaseName(columnData);
                columns.add(
                        new ColumnInfo(
                                columnData,
                                dbName,
                                true));
            }
            workbook.close();
            return columns;

    }

    public List<Map<String, Object>> getExcelData(MultipartFile file) throws Exception {

        List<Map<String, Object>> data = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);

        Row headerRow = sheet.getRow(0);

        DataFormatter formatter = new DataFormatter();

        List<String> headers = new ArrayList<>();

        int lastColumn = headerRow.getLastCellNum();

        for (int i = 0; i < lastColumn; i++) {
            Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            String columnData = "";
            if (cell != null) {
                columnData = formatter.formatCellValue(cell);
            }

            if (columnData.trim().isEmpty() || columnData.trim() == "" || columnData.trim() == null) {
                columnData = "empty " + i;
            }
            headers.add(columnData);
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            Map<String, Object> rowData = new LinkedHashMap<>();

            for (int j = 0; j < headers.size(); j++) {

                Cell cell = row.getCell(j);

                rowData.put(
                        headers.get(j),
                        getCellValue(cell));

            }
            data.add(rowData);
        }

        workbook.close();

        return data;
    }

    private Object getCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().toString();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            default:
                return null;
        }
    }

    private String convertToDatabaseName(String name) {

        return name
                .trim()
                .toLowerCase()
                .replace(" ", "_");

    }

}