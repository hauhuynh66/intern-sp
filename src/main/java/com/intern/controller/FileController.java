package com.intern.controller;

import com.intern.misc.UploadFile;
import com.intern.model.Admin;
import com.intern.model.UploadHistory;
import com.intern.repository.UploadHistoryRepository;
import com.intern.utils.ExcelService;
import com.intern.utils.Excel;
import com.intern.utils.Utils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    private Excel excel;
    @Autowired
    private ExcelService dex;
    @Autowired
    private UploadHistoryRepository uploadHistoryRepository;
    @Autowired
    private Utils utils;
    @PostMapping("/manage/upload")
    @ResponseBody
    private String uploadFile(@ModelAttribute UploadFile uploadFile)throws IOException {
        List<UploadHistory> uploadHistories = uploadHistoryRepository.findByAdmin_Mail(utils.getCurrentPrincipal());
        if(uploadHistories!=null){
            for(UploadHistory uploadHistory:uploadHistories){
                uploadHistoryRepository.delete(uploadHistory);
            }
        }
        String description = uploadFile.getDescription();
        System.out.println("Description : " +description);

        InputStream inputStream = uploadFile.getFile().getInputStream();
        File directory = new File("./src/main/resources/files");
        String path = directory.getAbsolutePath();
        String location = path +"\\"+ uploadFile.getFile().getOriginalFilename();
        System.out.println("File Location : " + location);

        FileOutputStream fileOutputStream = new FileOutputStream(location);
        int ch=0;
        while((ch=inputStream.read())!=-1){
            fileOutputStream.write(ch);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        readAndDelete(location);
        return "Success";
    }

    @GetMapping("/manage/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> download(@RequestParam String fileName)throws IOException{
        System.out.println(fileName);
        File dirrectory = new File("./src/main/resources/files");
        String path = dirrectory.getAbsolutePath();
        String location = path + "\\" + fileName;
        System.out.println(location);
        File file = new File(location);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(location));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+file.getName())
                .body(resource);
    }
    @GetMapping("/manage/getErrors")
    @ResponseBody
    private List<UploadHistory> getError(){
        List<UploadHistory> error = uploadHistoryRepository.findByAdmin_Mail(utils.getCurrentPrincipal());
        return error;
    }
    private void readAndDelete(String filePath)throws FileNotFoundException{
        Workbook workbook = excel.readInputFile(filePath);
        if(excel.getSheetByName(workbook,"School Code")!=null){
            dex.loadSchoolCode(filePath);
        }
        if(excel.getSheetByName(workbook,"Event Code")!=null){
            dex.loadEvent(filePath);
        }
        if(excel.getSheetByName(workbook,"Data List")!=null){
            dex.loadDataList(filePath);
        }
        if(excel.getSheetByName(workbook,"School Code")==null
                &&excel.getSheetByName(workbook,"Event Code")==null
                &&excel.getSheetByName(workbook,"Data List")==null){
            throw new FileNotFoundException("No sheet to read");
        }
        File file = new File(filePath);
        if(file.isDirectory()){
            throw new FileNotFoundException("File Not Found");
        }
        try{
            file.delete();
            System.out.println("Success Fully Delete File");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
