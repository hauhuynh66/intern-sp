package com.intern.utils;

import com.intern.model.Candidate;
import com.intern.model.Event;
import com.intern.model.Status;
import com.intern.repository.StatusRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Excel {
    @Autowired
    private StatusRepository statusRepository;
    public Map<Integer,List<String>> readExcel(String filePath,String sheetName){
        Workbook workbook = readInputFile(filePath);
        Sheet sheet = getSheetByName(workbook,sheetName);
        if(sheet==null){
            System.out.println("Can't Find Sheet Named : "+ sheetName);
            return new HashMap<>();
        }else{
            System.out.println("Successully Read Sheet Named : "+ sheetName);
            return readSheet(sheet);
        }
    }

    public Workbook readInputFile(String filePath){
        try {
            FileInputStream file = new FileInputStream(new File(filePath));
            return WorkbookFactory.create(file);
        }catch (FileNotFoundException f){
            System.out.println("UploadFile Not Found : " + filePath);
            return null;
        }catch (IOException io){
            System.out.println("IO Exception : " +filePath);
            return null;
        }catch (InvalidFormatException iv){
            System.out.println("Invalid Format Exception : "+ filePath);
            return null;
        }
    }
    public Map<Integer,List<String>> readSheet(Sheet sheet){
        DataFormatter dataFormatter = new DataFormatter();
        Map<Integer,List<String>> data = new HashMap<>();
        int i=0;
        for(Row row:sheet){
            data.put(i,new ArrayList<String>());
            for (Cell cell:row){
                data.get(i).add(readCell(cell)+"");
            }
            i++;
        }
        return data;
    }
    public Sheet getSheetByName(Workbook workbook,String sheetName){
        for(int i=0;i<workbook.getNumberOfSheets();i++){
            if(workbook.getSheetAt(i).getSheetName().equals(sheetName)){
                return workbook.getSheetAt(i);
            }
        }
        return null;
    }
    private boolean checkFormula(Cell cell){
        return cell.getCellTypeEnum() == CellType.FORMULA;
    }
    private Object readCell(Cell cell){
        DataFormatter dataFormatter = new DataFormatter();
        if(!checkFormula(cell)){
            return dataFormatter.formatCellValue(cell);
        }else {
            switch (cell.getCachedFormulaResultTypeEnum()){
                case NUMERIC:
                    if(DateUtil.isCellDateFormatted(cell)){
                        return cell.getDateCellValue();
                    }else {
                        return cell.getNumericCellValue();
                    }
                case STRING:
                    return cell.getStringCellValue();
                default:
                    return "";
            }
        }
    }
    public void writeToExcel(Map<Integer,List<String>> data,String sheetName,int rows){
        Workbook workbook = new XSSFWorkbook();
        try{
            Sheet sheet = workbook.createSheet(sheetName);
            Row header = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont font = ((XSSFWorkbook)workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short)16);
            font.setBold(true);
            headerStyle.setFont(font);
            Cell headerCell = header.createCell(0);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Code");

            headerCell = header.createCell(1);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Name");

            headerCell = header.createCell(2);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Note");

            headerCell = header.createCell(3);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Created Date");

            headerCell = header.createCell(4);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Last Modified");

            headerCell = header.createCell(5);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Active");

            headerCell = header.createCell(6);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Country Code");

            headerCell = header.createCell(7);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Hot Key");

            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);
            if(rows>data.size()||rows<=0){
                rows = data.size();
            }
            for(int i =1;i<rows;i++){
                Row row = sheet.createRow(i);
                for(int j=0;j<data.get(i).size();j++){
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(style);
                    cell.setCellValue(data.get(i).get(j));
                }
            }
            File file = new File(".");
            String path = file.getAbsolutePath();
            String fileLoc = path.substring(0,path.length()-1)+"temp.xlsx";
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileLoc);
                workbook.write(fileOutputStream);
            }catch (IOException io){
                System.out.println("IO Exception : Error Create Output Stream");
            }
        }finally {
            if(workbook!=null){
                try {
                    workbook.close();
                }catch (IOException io){
                    System.out.println("IO Exception : Error Closing Workbook");
                }

            }
        }
    }
    public void writeCandidateList(List<Candidate> candidates,String fileName){
        Workbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("Data List");
            Row header = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont font = ((XSSFWorkbook)workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short)16);
            font.setBold(true);
            headerStyle.setFont(font);
            Cell headerCell = header.createCell(0);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Account");

            headerCell = header.createCell(1);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Name");

            headerCell = header.createCell(2);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("University");

            headerCell = header.createCell(3);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Faculty");

            headerCell = header.createCell(4);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("DOB");

            headerCell = header.createCell(5);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Gender");

            headerCell = header.createCell(6);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Email");

            headerCell = header.createCell(7);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Phone");

            headerCell = header.createCell(8);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Facebook");

            headerCell = header.createCell(9);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Graduation Date");

            headerCell = header.createCell(10);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("FullTime Working");

            headerCell = header.createCell(11);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Event");

            headerCell = header.createCell(12);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Status");

            CellStyle style = workbook.createCellStyle();

            style.setWrapText(true);
            int i=1;
            for(Candidate candidate:candidates){
                List<Status> statusList = statusRepository.findByCandidate(candidate);
                int j=0;
                for(Status status:statusList){
                    Row row = sheet.createRow(i+j);
                    if(j==0){
                        Cell cell = row.createCell(0);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getAccount());

                        cell = row.createCell(1);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getName());

                        cell = row.createCell(2);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getUniversity().getName());

                        cell = row.createCell(3);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getFaculty().getCode());

                        cell = row.createCell(4);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getDateOfBirth()+"");

                        cell = row.createCell(5);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getGender());

                        cell = row.createCell(6);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getEmail());

                        cell = row.createCell(7);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getPhone());

                        cell = row.createCell(8);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getFacebook());

                        cell = row.createCell(9);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getGraduationDate()+"");

                        cell = row.createCell(10);
                        cell.setCellStyle(style);
                        cell.setCellValue(candidate.getFulltimeDate()+"");

                        cell = row.createCell(11);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getEvent().getCourseCode());

                        cell = row.createCell(12);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getStatus()+"");

                        cell = row.createCell(13);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getFinalGrade()+"");

                        cell = row.createCell(14);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getCompletionLevel()+"");

                        cell = row.createCell(15);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getCertificateId()+"");
                    }else {
                        Cell cell = row.createCell(11);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getEvent().getCourseCode());

                        cell = row.createCell(12);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getStatus()+"");

                        cell = row.createCell(13);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getFinalGrade()+"");

                        cell = row.createCell(14);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getCompletionLevel()+"");

                        cell = row.createCell(15);
                        cell.setCellStyle(style);
                        cell.setCellValue(status.getCertificateId()+"");
                    }

                    j++;
                }
                i+=j;
            }
            File directory = new File("./src/main/resources/files");
            String path = directory.getAbsolutePath();
            String fileLoc = path +"\\"+ fileName;
            System.out.println(fileLoc);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileLoc);
                workbook.write(fileOutputStream);
            }catch (IOException io){
                System.out.println("IO Exception : Error Create Output Stream");
            }
        }finally {
            try {
                workbook.close();
            }catch (IOException io){
                System.out.println("IO Exception : Error Closing Workbook");
            }

        }

    }
    public void writeEventList(List<Event> events,String fileName){
        Workbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("Event Code");
            Row header = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont font = ((XSSFWorkbook)workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short)11);
            font.setBold(true);
            headerStyle.setFont(font);
            Cell headerCell = header.createCell(0);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Site");

            headerCell = header.createCell(1);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Course Code");

            headerCell = header.createCell(2);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Course Name");

            headerCell = header.createCell(3);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Subject type");

            headerCell = header.createCell(4);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Sub-Subject Type");

            headerCell = header.createCell(5);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Format Type");

            headerCell = header.createCell(6);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Supplier/Partner");

            headerCell = header.createCell(7);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Planned Start Date");

            headerCell = header.createCell(8);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Planned End Date");

            headerCell = header.createCell(9);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Planned number of students");

            headerCell = header.createCell(10);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Actual Start Date");

            headerCell = header.createCell(11);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Actual End Date");

            headerCell = header.createCell(12);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue("Actual number of trainees");

            CellStyle style = workbook.createCellStyle();

            style.setWrapText(true);
            int i=1;
            for(Event event:events){
                List<Status> statusList = statusRepository.findByEventAndStatus(event,"Passed");
                Row row = sheet.createRow(i);

                Cell cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue(event.getSite().getSite());

                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(event.getCourseCode());

                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue(event.getProgram().getName());

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(event.getSubjectType());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(event.getSkill().getSkillName());

                cell = row.createCell(5);
                cell.setCellStyle(style);
                cell.setCellValue(event.getFormat());

                cell = row.createCell(6);
                cell.setCellStyle(style);
                cell.setCellValue(event.getUniversity().getName());

                cell = row.createCell(7);
                cell.setCellStyle(style);
                cell.setCellValue(event.getPlannedStartDate()+"");

                cell = row.createCell(8);
                cell.setCellStyle(style);
                cell.setCellValue(event.getPlannedEndDate()+"");

                cell = row.createCell(9);
                cell.setCellStyle(style);
                cell.setCellValue(event.getStatuses().size());

                cell = row.createCell(10);
                cell.setCellStyle(style);
                cell.setCellValue(event.getActualStartDate()+"");

                cell = row.createCell(11);
                cell.setCellStyle(style);
                cell.setCellValue(event.getActualEndDate()+"");

                cell = row.createCell(12);
                cell.setCellStyle(style);
                cell.setCellValue(statusList.size());

                i++;
            }
            File directory = new File("./src/main/resources/files");
            String path = directory.getAbsolutePath();
            String fileLoc = path +"\\"+ fileName;
            System.out.println(fileLoc);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileLoc);
                workbook.write(fileOutputStream);
            }catch (IOException io){
                System.out.println("IO Exception : Error Create Output Stream");
            }
        }finally {
            try {
                workbook.close();
            }catch (IOException io){
                System.out.println("IO Exception : Error Closing Workbook");
            }

        }

    }
}
