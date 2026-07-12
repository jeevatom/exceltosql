package excel.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnInfo {

    private String excelName;
    private String dbName;
    private boolean selected;

}