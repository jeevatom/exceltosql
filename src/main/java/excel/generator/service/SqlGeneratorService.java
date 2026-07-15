package excel.generator.service;


import java.io.FileInputStream;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;


import org.apache.poi.ss.usermodel.*;

import org.springframework.stereotype.Service;


import excel.generator.model.ColumnMapping;



@Service
public class SqlGeneratorService {



public String generateInsertSql(
        String tableName,
        List<ColumnMapping> columns,
        String filePath
) throws Exception {



    List<ColumnMapping> selectedColumns =
            columns.stream()
            .filter(ColumnMapping::isSelected)
            .toList();



    if(selectedColumns.isEmpty()){
        return "No columns selected";
    }



    String columnNames =
            selectedColumns.stream()
            .map(ColumnMapping::getDbName)
            .collect(Collectors.joining(", "));




    StringBuilder sql =
            new StringBuilder();



    sql.append("CREATE TABLE ")
       .append(tableName)
       .append(" (\n");



    StringJoiner joiner =
            new StringJoiner(",\n");



    selectedColumns.forEach(
            c -> joiner.add(
                    c.getDbName()
                    +" VARCHAR(255)"
            )
    );


    sql.append(joiner);

    sql.append("\n);\n\n");




    Workbook workbook =
            WorkbookFactory.create(
                    new FileInputStream(filePath)
            );


    Sheet sheet =
            workbook.getSheetAt(0);



    Row header =
            sheet.getRow(0);



    DataFormatter formatter =
            new DataFormatter();



    for(int i=1;i<=sheet.getLastRowNum();i++){


        Row row =
                sheet.getRow(i);



        sql.append("INSERT INTO ")
           .append(tableName)
           .append("(")
           .append(columnNames)
           .append(") VALUES (");



        for(int j=0;j<selectedColumns.size();j++){


            ColumnMapping column =
                    selectedColumns.get(j);


            int index =
                    columns.indexOf(column);



            Cell cell =
                    row.getCell(index);



            String value =
                    cell==null?
                    null:
                    formatter.formatCellValue(cell);



            if(value==null || value.isEmpty()){

                sql.append("NULL");

            }
            else{

                sql.append("'")
                   .append(
                    value.replace("'","''")
                   )
                   .append("'");

            }



            if(j < selectedColumns.size()-1){
                sql.append(",");
            }

        }


        sql.append(");\n");

    }


    workbook.close();


    return sql.toString();

}


}