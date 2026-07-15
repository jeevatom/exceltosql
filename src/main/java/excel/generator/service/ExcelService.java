package excel.generator.service;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import excel.generator.model.ColumnInfo;


@Service
public class ExcelService {


    public List<ColumnInfo> getColumns(
            MultipartFile file) throws Exception {


        List<ColumnInfo> columns = new ArrayList<>();


        Workbook workbook =
                WorkbookFactory.create(file.getInputStream());


        Sheet sheet =
                workbook.getSheetAt(0);


        Row headerRow =
                sheet.getRow(0);


        DataFormatter formatter =
                new DataFormatter();


        int lastColumn =
                headerRow.getLastCellNum();



        for(int i=0;i<lastColumn;i++){


            Cell cell =
                    headerRow.getCell(
                            i,
                            Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
                    );


            String name="";


            if(cell!=null){
                name =
                formatter.formatCellValue(cell);
            }


            if(name.trim().isEmpty()){
                name="empty_"+i;
            }


            columns.add(
                    new ColumnInfo(
                            name,
                            convertToDatabaseName(name),
                            true
                    )
            );

        }


        workbook.close();


        return columns;
    }



public String saveFile(MultipartFile file) throws Exception {

    Path uploadDir = Paths.get("uploads");

    if (!Files.exists(uploadDir)) {
        Files.createDirectories(uploadDir);
    }

    String fileName =
            System.currentTimeMillis()
            + "_"
            + file.getOriginalFilename();

    Path filePath = uploadDir.resolve(fileName);

    Files.copy(
            file.getInputStream(),
            filePath,
            StandardCopyOption.REPLACE_EXISTING
    );

    return filePath.toAbsolutePath().toString();
}





    private String convertToDatabaseName(
            String name){

        return name
                .trim()
                .toLowerCase()
                .replace(" ","_");
    }

}