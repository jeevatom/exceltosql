package excel.generator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import excel.generator.Dto.ColumnMappingRequest;
import excel.generator.model.ColumnInfo;
import excel.generator.service.ExcelService;
import excel.generator.service.SqlGeneratorService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ExcelController {

    private final ExcelService excelService;
    private final SqlGeneratorService sqlGeneratorService;

    public ExcelController(
            ExcelService excelService,
            SqlGeneratorService sqlGeneratorService) {

        this.excelService = excelService;
        this.sqlGeneratorService = sqlGeneratorService;
    }


    @PostMapping("/upload")
    public String uploadExcel(
            @RequestParam MultipartFile file,
            HttpSession session,
            @RequestParam String tableName,
            Model model) throws Exception {


        // Read only headers
        List<ColumnInfo> columns = excelService.getColumns(file);


        // Save file instead of storing excel data
        String filePath = excelService.saveFile(file);


        session.setAttribute(
                "filePath",
                filePath
        );


        model.addAttribute(
                "columns",
                columns
        );


        tableName = tableName
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "_");


        model.addAttribute(
                "tableName",
                tableName
        );


        return "mapping";
    }



    @PostMapping("/generateSql")
    public String generateSql(
            @RequestParam String tableName,
            @RequestParam String database,
            @ModelAttribute ColumnMappingRequest request,
            HttpSession session,
            Model model) throws Exception {



        String filePath =
                (String) session.getAttribute("filePath");



        String sql =
                sqlGeneratorService.generateInsertSql(
                        tableName,
                        request.getColumns(),
                        filePath
                );


        model.addAttribute(
                "sql",
                sql
        );


        return "sql-result";
    }

}