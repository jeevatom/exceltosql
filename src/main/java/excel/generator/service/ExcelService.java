package excel.generator.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import excel.generator.model.ColumnInfo;

@Service
public class ExcelService {


    public List<ColumnInfo> getColumns(MultipartFile file) throws Exception {

        List<ColumnInfo> columns = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);

        Row headerRow = sheet.getRow(0);


        for(Cell cell : headerRow) {

            String excelName = cell.getStringCellValue();

            String dbName = convertToDatabaseName(excelName);


            columns.add(
                    new ColumnInfo(
                            excelName,
                            dbName,
                            true
                    )
            );
        }


        workbook.close();

        return columns;
    }



    public List<Map<String,Object>> getExcelData(MultipartFile file) throws Exception {


        List<Map<String,Object>> data = new ArrayList<>();


        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);


        Row headerRow = sheet.getRow(0);


        List<String> headers = new ArrayList<>();


        for(Cell cell : headerRow) {

            headers.add(cell.getStringCellValue());

        }



        for(int i = 1; i <= sheet.getLastRowNum(); i++) {


            Row row = sheet.getRow(i);


            Map<String,Object> rowData = new LinkedHashMap<>();


            for(int j = 0; j < headers.size(); j++) {


                Cell cell = row.getCell(j);


                rowData.put(
                        headers.get(j),
                        getCellValue(cell)
                );

            }


            data.add(rowData);

        }


        workbook.close();


        return data;
    }



    private Object getCellValue(Cell cell) {


        if(cell == null) {
            return null;
        }


        switch(cell.getCellType()) {


            case STRING:
                return cell.getStringCellValue();


            case NUMERIC:
                return cell.getNumericCellValue();


            case BOOLEAN:
                return cell.getBooleanCellValue();


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