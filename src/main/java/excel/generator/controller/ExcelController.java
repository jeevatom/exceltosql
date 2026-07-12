package excel.generator.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import excel.generator.model.ColumnInfo;
import excel.generator.Dto.ColumnMappingRequest;
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

        List<ColumnInfo> columns = excelService.getColumns(file);

        List<Map<String, Object>> excelData = excelService.getExcelData(file);

        session.setAttribute(
                "excelData",
                excelData);

        model.addAttribute(
                "columns",
                columns);

        tableName = tableName.trim()
                .toLowerCase()
                .replaceAll("\\s+", "_");
        model.addAttribute(
                "tableName",
                tableName);

        return "mapping";
    }

    @PostMapping("/generateSql")
    public String generateSql(
            @RequestParam String tableName,
            @RequestParam String database,
            @ModelAttribute ColumnMappingRequest request,
            HttpSession session,
            Model model) {

        List<Map<String, Object>> excelData = (List<Map<String, Object>>) session.getAttribute("excelData");

        String sql = sqlGeneratorService.generateInsertSql(
                tableName,
                request.getColumns(),
                excelData);

        model.addAttribute(
                "sql",
                sql);

        return "sql-result";
    }

}