package excel.generator.Dto;

import java.util.List;

import excel.generator.model.ColumnMapping;

public class ColumnMappingRequest {

    private String tableName;

    private String database;

    private List<ColumnMapping> columns;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }


    public List<ColumnMapping> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMapping> columns) {
        this.columns = columns;
    }
}