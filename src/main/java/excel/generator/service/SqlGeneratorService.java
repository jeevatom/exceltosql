package excel.generator.service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import excel.generator.model.ColumnMapping;



@Service
public class SqlGeneratorService {



    public String generateInsertSql(
            String tableName,
            List<ColumnMapping> columns,
            List<Map<String,Object>> excelData) {



        List<ColumnMapping> selectedColumns =
                columns.stream()
                .filter(ColumnMapping::isSelected)
                .toList();



        if(selectedColumns.isEmpty()) {

            return "No columns selected";

        }



        String columnNames =
                selectedColumns.stream()
                .map(ColumnMapping::getDbName)
                .collect(Collectors.joining(", "));



        StringBuilder sql = new StringBuilder();



        for(Map<String,Object> row : excelData) {


            sql.append("INSERT INTO ")
               .append(tableName)
               .append(" (")
               .append(columnNames)
               .append(") VALUES (");



            String values =
                    selectedColumns.stream()
                    .map(column -> {


                        Object value =
                                row.get(column.getExcelName());



                        if(value == null) {

                            return "NULL";

                        }


                        return "'" +
                                value.toString()
                                .replace("'", "''")
                                +
                                "'";


                    })
                    .collect(Collectors.joining(", "));



            sql.append(values);


            sql.append(");\n\n");

        }



        return sql.toString();

    }

}