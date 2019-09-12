package com.intern.utils;

import com.intern.model.*;
import com.intern.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Component
public class ExcelService {
    @Autowired
    private Excel excel;
    @Autowired
    private Converter converter;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UploadHistoryRepository uploadHistoryRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private Utils utils;
    private void saveToUploadHistory(String content,String row){
        UploadHistory uploadHistory = new UploadHistory();
        uploadHistory.setAdmin(adminRepository.findByMail(utils.getCurrentPrincipal()));
        uploadHistory.setType("Error");
        uploadHistory.setContent(content);
        uploadHistory.setRow(row);
        uploadHistoryRepository.save(uploadHistory);
    }
    public void loadSchoolCode(String filePath){
        String sheetName = "School Code";
        Map<Integer,List<String>> data = excel.readExcel(filePath,sheetName);
        System.out.println("Successfully Read "+ data.size()+ " Columns From Sheet : "+ sheetName);
        for(int i=1;i<data.size();i++){
            String siteName = data.get(i).get(1);
            if(checkCell(siteName)){
                System.out.println("Site is empty or error");
                saveToUploadHistory("Site is empty or error",(i+1)+"");
            }else {
                Site site = siteRepository.findBySite(data.get(i).get(1));
                if(site!=null){
                    String universityCode = data.get(i).get(5);
                    if(checkCell(universityCode)){
                        System.out.println("University code is empty or error");
                        saveToUploadHistory("University code is empty or error",(i+1)+"");
                    }else {
                        String universityName = converter.removeQuote(data.get(i).get(3));
                        if(!checkCell(universityName)){
                            int hotKey = converter.stringToInt(data.get(i).get(6));
                            University university = insertUniversity(universityCode,universityName,hotKey,site);
                            String facultyName = data.get(i).get(4);
                            String facultyCode = data.get(i).get(7);
                            if(!checkCell(facultyName)&&!checkCell(facultyCode)){
                                insertFaculty(university,facultyName,facultyCode);
                            }
                        }
                    }
                }
            }
        }
    }
    private boolean checkCell(String cellValue){
        return cellValue.isEmpty() || cellValue.equals("#N/A");
    }
    private University insertUniversity(String universityCode,String universityName,int hotKey,Site site){
        University university = universityRepository.findByCode(universityCode);
        if(university==null){
            university = new University();
            university.setSite(site);
            university.setCode(universityCode);
            university.setName(universityName);
            if(hotKey==0){
                university.setHotKey(null);
            }else{
                university.setHotKey(hotKey+"");
            }
            universityRepository.save(university);
        }
        return university;
    }
    private void insertFaculty(University university,String facultyName,String facultyCode){
        Faculty faculty = facultyRepository.findByCodeAndName(facultyCode,facultyName);
        if(faculty==null){
            faculty = new Faculty();
            faculty.setCode(facultyCode);
            faculty.setName(facultyName);
            facultyRepository.save(faculty);
        }
        Set<Faculty> faculties = university.getFaculties();
        if(faculties!=null){
            boolean isContains = false;
            for (Faculty f:faculties){
                if(f==faculty){
                    isContains = true;
                    break;
                }
            }
            if(!isContains){
                faculties.add(faculty);
            }
        }else{
            faculties = new HashSet<>();
            faculties.add(faculty);
        }
        universityRepository.save(university);
    }
    public void loadEvent(String filePath){
        String sheetName = "Event Code";
        Map<Integer,List<String>> eventList = excel.readExcel(filePath,sheetName);
        System.out.println("Successfully Read "+ eventList.size()+ " Columns From Sheet : "+ sheetName);
        for(int i=3;i<eventList.size();i++) {
            if(eventList.get(i).size()>2){
                String eventCode = eventList.get(i).get(1);
                if (!checkCell(eventCode)) {
                    Event event = eventRepository.findByCourseCode(eventList.get(i).get(1));
                    if(event==null) {
                        event = new Event();
                        University university = universityRepository.findByCode(eventList.get(i).get(38));
                        Set<Faculty> faculties = university.getFaculties();
                        boolean isContains = false;
                        for (Faculty f : faculties) {
                            if (f.getCode().equals(eventList.get(i).get(40))) {
                                isContains = true;
                                event.setFaculty(f);
                                break;
                            }
                        }
                        if(!isContains){
                            System.out.println("Wrong faculty");
                            saveToUploadHistory("Wrong faculty",(i+1)+"");
                        }else{
                            event.setSubjectType(eventList.get(i).get(3));
                            event.setFormat(eventList.get(i).get(5));
                            Date planStartDate = converter.stringToDate(eventList.get(i).get(7),"dd-MMM-yyyy");
                            if(planStartDate!=null){
                                event.setPlannedStartDate(planStartDate);
                            }
                            Date planEndDate = converter.stringToDate(eventList.get(i).get(8),"dd-MMM-yyyy");
                            if(planEndDate!=null){
                                event.setPlannedEndDate(planEndDate);
                            }
                            Date actualStartDate = converter.stringToDate(eventList.get(i).get(13),"dd-MMM-yyyy");
                            if(actualStartDate!=null){
                                event.setActualStartDate(actualStartDate);
                            }
                            Date actualEndDate = converter.stringToDate(eventList.get(i).get(14),"dd-MMM-yyyy");
                            if(actualEndDate!=null){
                                event.setActualEndDate(actualEndDate);
                            }
                            Date updateDate = converter.stringToDate(eventList.get(i).get(13),"dd-MMM-yyyy");
                            if(updateDate!=null){
                                event.setUpdateDate(updateDate);
                            }
                            event.setPlannedExpense(converter.stringToDouble(eventList.get(i).get(11)));
                            event.setBudgetCode(eventList.get(i).get(12));
                            event.setActualLearningTime(converter.stringToInt(eventList.get(i).get(15)));
                            event.setActualExpense(converter.stringToDouble(eventList.get(i).get(19)));
                            event.setTrainingFeedback(eventList.get(i).get(20));
                            event.setFeedbackContent(eventList.get(i).get(21));
                            event.setFeedbackTeacher(eventList.get(i).get(22));
                            event.setFeedbackOrganization(eventList.get(i).get(23));
                            event.setSite(siteRepository.findBySite(eventList.get(i).get(0)));
                            event.setProgram(programRepository.findByName(eventList.get(i).get(2)));
                            event.setSkill(skillRepository.findBySkillName(eventList.get(i).get(4)));
                            event.setUniversity(university);
                            event.setCourseCode(eventList.get(i).get(1));
                            eventRepository.save(event);
                        }
                    }
                }
            }
        }
    }
    public void loadDataList(String fileName){
        String sheetName = "Data List";
        Map<Integer,List<String>> data = excel.readExcel(fileName,sheetName);
        System.out.println("Successfully Read "+ data.size()+ " Columns From Sheet : "+ sheetName);
        for(int i=3;i<data.size();i++){
            if(data.get(i).size()>8){
                Event event = eventRepository.findByCourseCode(data.get(i).get(13));
                if(event==null){
                    System.out.println("Event not found in event list");
                    saveToUploadHistory("Event not found in event list",(i+3)+"");
                }else{
                    String stat = data.get(i).get(20);
                    Double f_grade = converter.stringToDouble(data.get(i).get(21));
                    String c_level = data.get(i).get(22);
                    String c_id = data.get(i).get(23);
                    Candidate candidate = candidateRepository.findByNameAndEmail(data.get(i).get(2),data.get(i).get(7));
                    if(candidate==null){
                        candidate = new Candidate();
                    }
                    University university = universityRepository.findByName(converter.removeQuote(data.get(i).get(3)));
                    if(!checkCell(data.get(i).get(2))){
                        candidate.setName(data.get(i).get(2));
                    }
                    if(!checkCell(data.get(i).get(7))){
                        candidate.setEmail(data.get(i).get(7));
                    }
                    if(!checkCell(data.get(i).get(1))){
                        candidate.setAccount(data.get(i).get(1));
                    }

                    if(!checkCell(data.get(i).get(0))){
                        candidate.setNationalId(converter.stringToInt(data.get(i).get(0)));
                    }

                    if(!checkCell(data.get(i).get(5))){
                        candidate.setDateOfBirth(converter.stringToDate(data.get(i).get(5),"EEE MMM dd hh:mm:ss z yyyy"));
                    }
                    if(university!=null){
                        candidate.setUniversity(university);
                        if(!checkCell(data.get(i).get(4))){
                            Set<Faculty> faculties = university.getFaculties();
                            boolean isContains = false;
                            for(Faculty faculty:faculties){
                                if(faculty.getCode().equals(data.get(i).get(4))){
                                    candidate.setFaculty(faculty);
                                    isContains = true;
                                    break;
                                }
                            }
                            if(!isContains){
                                System.out.println("Wrong faculty");
                                saveToUploadHistory("Wrong faculty",(i+3)+"");
                            }else{
                                candidate.setSkill(event.getSkill());
                                if(!checkCell(data.get(i).get(6))){
                                    candidate.setGender(data.get(i).get(6));
                                }
                                if(!checkCell(data.get(i).get(8))){
                                    candidate.setPhone(data.get(i).get(8));
                                }
                                if(!checkCell(data.get(i).get(9))){
                                    candidate.setFacebook(data.get(i).get(9));
                                }
                                if(!checkCell(data.get(i).get(10))){
                                    candidate.setGraduationDate(converter.stringToDate(data.get(i).get(10),"yyyy"));
                                }
                                if(!checkCell(data.get(i).get(11))){
                                    candidate.setGraduationDate(converter.stringToDate(data.get(i).get(11),"yyyy"));
                                }
                                candidateRepository.save(candidate);
                            }
                        }else {
                            System.out.println("Wrong faculty");
                            saveToUploadHistory("Wrong faculty",(i+3)+"");
                        }
                    }else{
                        System.out.println("Wrong university");
                        saveToUploadHistory("Wrong university",(i+3)+"");
                    }
                    insertStatus(candidate,event,stat,c_level,c_id,f_grade);
                }
            }
            else {
                System.out.println("Not enough information");
                saveToUploadHistory("Not enough information",(i+3)+"");
            }
        }
    }
    private void insertStatus(Candidate candidate,Event event,String s,String c_level,String c_id,Double f_grade){
        if(candidate==null||event==null){
            return;
        }else{
            Status status = statusRepository.findByEventAndCandidate(event,candidate);
            if(status==null) {
                status = new Status();
                status.setCandidate(candidate);
                status.setEvent(event);
            }
            if(s!=null){
                status.setStatus(s);
            }
            if(c_level!=null){
                status.setCompletionLevel(c_level);
            }
            if(c_id!=null){
                status.setCertificateId(c_id);
            }
            if(f_grade!=null){
                status.setFinalGrade(f_grade);
            }
            statusRepository.save(status);
        }
    }
}
