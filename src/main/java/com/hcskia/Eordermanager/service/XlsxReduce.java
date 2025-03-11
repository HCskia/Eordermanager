package com.hcskia.Eordermanager.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class XlsxReduce {
    public static List<Map<String,List<List<String>>>> XlsxToJson(ArrayList<String> XlsxFiles,String path){
        List<Map<String,List<List<String>>>> finalJson = new ArrayList<>();
        List<List<String>> pddJson = new ArrayList<>();
        List<List<String>> qnJson = new ArrayList<>();
        int pformStatus = 0;
        for(String xlsxfile:XlsxFiles){
            try(FileInputStream fis = new FileInputStream(path+"/"+xlsxfile)) {
                XSSFWorkbook workbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator=sheet.iterator();
                while (rowIterator.hasNext()){
                    Row row = rowIterator.next();
                    Integer rowCursor = row.getRowNum();
                    List<String> tempList = new ArrayList<>();
                    Iterator<Cell> cellIterator=row.iterator();
                    while (cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        Integer cellCursor = cell.getColumnIndex();
                        if((rowCursor==0)&&(cellCursor==0)){
                            switch (cell.getStringCellValue()) {
                                case ("订单编号") -> pformStatus = 1;
                                case ("店铺名称") -> pformStatus = 2;
                            }
                        }
                        tempList.add(cell.getStringCellValue());
                    }
                    switch (pformStatus) {
                        case 1 -> pddJson.add(tempList);
                        case 2 -> qnJson.add(tempList);
                    }
                }
            }catch (IOException e){
                return null;
            }
        }
        finalJson.add(new HashMap<String,List<List<String>>>(){{put("pdd",pddJson);put("qn",qnJson);}});
        return finalJson;
    }
}
