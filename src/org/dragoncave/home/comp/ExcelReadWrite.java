/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

import java.io.FileInputStream;
import java.io.IOException;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
/**
 *
 * @author Rider1
 */

public class ExcelReadWrite {
    private int numSheets = 0;
    private int[] numRows;
    private int[][] numCells;
    
    private static XSSFWorkbook excelWB;
    
    private String cellValue;
    
    public ExcelReadWrite(String filename){
        cellValue=null;
        try{
            readFile(filename);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void readFile(String filename) throws IOException{
        FileInputStream fis = new FileInputStream(filename);
        try {
            excelWB =  new XSSFWorkbook(fis);
            initWB();
//            System.out.println("file found");
        } finally {
            fis.close();
        }
    }
    private void initWB(){
        numSheets = excelWB.getNumberOfSheets();
        numRows = new int[numSheets];
        numCells = new int[numSheets][];
        // row per sheet- index = sheet number, value = #rows
        int skippedRows=0;
        for (int x = 0; x < numSheets; x++){
            // returns number of rows per sheet
            numRows[x] = excelWB.getSheetAt(x).getPhysicalNumberOfRows();
            numCells[x]= new int[excelWB.getSheetAt(x).getPhysicalNumberOfRows()];
            skippedRows=0;
            // iterate throw rows to get number of cells
            for (int y = 0; y < excelWB.getSheetAt(x).getPhysicalNumberOfRows(); y++){
            //    System.out.println("SheetIndex:\t"+x+"row:\t"+(y+1));
            //    if(excelWB.getSheetAt(x).getRow(y+skippedRows) == null){
                while(checkForNullRow(x,y+skippedRows)){
                    skippedRows++;
            //        System.out.println("sheet index:\t"+x+"\tempty row: skippedRows:\t"+skippedRows);
            //        continue;
                }
                numCells[x][y] = excelWB.getSheetAt(x).getRow(y+skippedRows).getPhysicalNumberOfCells();
            //    System.out.println("sheets:\t"+x+"\trow index:\t"+(y+skippedRows)+"\tCells:\t"+excelWB.getSheetAt(x).getRow(y+skippedRows).getPhysicalNumberOfCells());
            }
        }
    }
    
    public int getNumSheets(){
        return numSheets;
    }
    public int getNumRows(int sheetIndex){
        return numRows[sheetIndex];
    }
    public int getNumCells(int sheetIndex, int rowIndex){
        return numCells[sheetIndex][rowIndex];
    }
    
    public void getStructure(){
        System.out.println(numCells.length);
    }
    
    public String getCellvalue(int sheetIndex, int rowIndex, int cellIndex){
        int skippedRows=0;
        boolean skippedRow = false;
        for (int x=0; x <= rowIndex; x++){
            while (checkForNullRow(sheetIndex,x+skippedRows)){
                skippedRows++;
            }
        }
        int skippedCells=0;
        for(int y=0; y <= cellIndex;y++){
            //if(checkForNullCell(sheetIndex,rowIndex+skippedRows,cellIndex+skippedCells)){
            //    skippedCells++;
                while(checkForNullCell(sheetIndex,rowIndex+skippedRows,cellIndex+skippedCells))
                    skippedCells++;
            //}
        }
//        System.out.println("skipped rows:\t"+skippedRows);
        XSSFCell cell = excelWB.getSheetAt(sheetIndex).getRow(rowIndex+skippedRows).getCell(cellIndex+skippedCells);
        cellValue = null;

            switch (cell.getCellType()) {

                case XSSFCell.CELL_TYPE_FORMULA:
                    cellValue = cell.getCellFormula();
                    break;

                case XSSFCell.CELL_TYPE_NUMERIC:
                    cellValue = Double.toString(cell.getNumericCellValue());
                    break;

                case XSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;

                default:
                    cellValue = "";
            }
        return cellValue;
    }
    public void close(){
        try{
            excelWB.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    private boolean checkForNullRow(int sheetIndex, int rowIndex){
        boolean nullRow = false;
        if(excelWB.getSheetAt(sheetIndex).getRow(rowIndex)==null)
            nullRow = true;
        
        return nullRow;
    }
    
    private boolean checkForNullCell(int sheetIndex, int rowIndex, int cellIndex){
        boolean nullCell = false;
        if(excelWB.getSheetAt(sheetIndex).getRow(rowIndex).getCell(cellIndex)==null)
            nullCell = true;
        
        return nullCell;
    }
}

