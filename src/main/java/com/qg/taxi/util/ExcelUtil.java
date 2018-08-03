package com.qg.taxi.util;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.qg.taxi.model.excel.CountModel;
import com.qg.taxi.model.excel.ExcelPropertyIndexModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：excel工具类
 * motto: All efforts are not in vain
 */
public class ExcelUtil {
    private static String excelPath = System.getProperty("user.dir")+"\\src\\main\\webapp\\take-taxi";
    private static List<String> times = Arrays.asList("geoHash",
            "00:00:00~01:00:00", "01:00:00~02:00:00", "02:00:00~03:00:00", "03:00:00~04:00:00",
            "04:00:00~05:00:00", "05:00:00~06:00:00", "06:00:00~07:00:00", "07:00:00~08:00:00",
            "08:00:00~09:00:00", "08:00:00~09:00:00", "09:00:00~10:00:00", "10:00:00~11:00:00",
            "11:00:00~12:00:00", "12:00:00~13:00:00", "13:00:00~14:00:00", "14:00:00~15:00:00",
            "15:00:00~16:00:00", "16:00:00~17:00:00", "17:00:00~18:00:00", "19:00:00~20:00:00",
            "20:00:00~21:00:00", "21:00:00~22:00:00", "22:00:00~23:00:00", "23:00:00~24:00:00");



    public static String createExcel(Map<String, List<CountModel>> geoHashMap) throws IOException {
        String excelName = UUID.randomUUID().toString();
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建excel表格
        XSSFSheet spreadSheet = workbook.createSheet("day");
        XSSFRow row;
        int rowId = 0 , cellId = 0;
        //创建第一行，第一行是类型行
        row = spreadSheet.createRow(rowId++);
        for (String time : times) {
            Cell cell = row.createCell(cellId++);
            cell.setCellValue(time);
        }

        for (Map.Entry<String, List<CountModel>> entry : geoHashMap.entrySet()){
            cellId = 0;
            row = spreadSheet.createRow(rowId++);
            Cell cell = row.createCell(cellId ++ );
            cell.setCellValue(entry.getKey());

            for (CountModel countModel : entry.getValue()) {
                Cell cell1 = row.createCell(countModel.getTimeRepre()+1);
                cell1.setCellValue(countModel.getCount());
            }
        }

        FileOutputStream out = new FileOutputStream(new File(excelPath+"\\"+excelName+".xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("导出完成");

        return "http://192.168.1.109:8086//"+excelName+".xlsx";
    }

    public static void writeData(String filePath, String[] titles, Map<String, double[]> map, String sheetName) {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        int rowIndex = 0;
        try {
            Row titleRow = sheet.createRow(rowIndex);
            int index = 0;
            for (String title : titles) {
                titleRow.createCell(index).setCellValue(title);
                index++;
            }
            rowIndex++;
            for (String key : map.keySet()) {
                Row row = sheet.createRow(rowIndex);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(key);
                for (int i = 1; i <= map.get(key).length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(map.get(key)[i - 1]);
                }
                rowIndex++;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeExcel(String filePath, List<ExcelPropertyIndexModel> list) throws FileNotFoundException {
        OutputStream out = new FileOutputStream(filePath);
        try {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            com.alibaba.excel.metadata.Sheet sheet1 = new com.alibaba.excel.metadata.Sheet(1, 0, ExcelPropertyIndexModel.class);
            writer.write(list, sheet1);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    public static List<String> getPlate() throws IOException {
        List<String> list = new ArrayList<>();
        Resource resource = new ClassPathResource(System.getProperty("user.dir") + "/src/main/resources/plate.xls");
        File file = resource.getFile();

        try {
            FileInputStream in = new FileInputStream(file);

            HSSFWorkbook wb = new HSSFWorkbook(in);

            Sheet sheet = wb.getSheet("sheet");
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();

            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                list.add(sheet.getRow(i).getCell(0).getStringCellValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
