package com.intern.service;

import com.intern.model.*;
import com.intern.repository.*;
import com.intern.utils.Converter;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CandidateService {
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private CustomValidationService customValidationService;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private Converter converter;
    public List<Status> filterByUniversity(List<Status> statusList,List<University> universities){
        if(universities==null){
            return statusList;
        }
        for(int i=0;i<statusList.size();i++){
            boolean isContain = false;
            for(University university:universities){
                if(statusList.get(i).getCandidate().getUniversity()==university){
                    isContain = true;
                    break;
                }
            }
            if(!isContain){
                statusList.remove(i);
                i--;
            }
        }
        return statusList;
    }
    public List<Status> filterByStatus(List<Status> statusList,List<String> statusName){
        if(statusName==null){
            return statusList;
        }
        for(int i=0;i<statusList.size();i++){
            boolean isContain = false;
            for (String s : statusName) {
                if (statusList.get(i).getStatus().equals(s)) {
                    isContain = true;
                    break;
                }
            }
            if(!isContain){
                statusList.remove(i);
                i--;
            }
        }
        return statusList;
    }
    public List<Status> filterByEvent(List<Status> statusList, List<String> events){
        for(int i=0;i<statusList.size();i++){
            boolean isContain = false;
            for(String event:events){
                if(statusList.get(i).getEvent().getCourseCode().equals(event)){
                    isContain = true;
                    break;
                }
            }
            if(!isContain){
                statusList.remove(i);
                i--;
            }

        }
        return statusList;
    }
    public List<Status> filterBySkill(List<Status> statusList, List<String> skillName){
        if(skillName==null){
            return statusList;
        }
        for(int i=0;i<statusList.size();i++){
            boolean isContain = false;
            for(String skill:skillName){
                if(statusList.get(i).getCandidate().getSkill().getSkillName().equals(skill)){
                    isContain = true;
                    break;
                }
            }
            if(!isContain){
                statusList.remove(i);
                i--;
            }

        }
        return statusList;
    }
    public List<Candidate> lookupCandidate(List<Status> statusList){
        List<Candidate> candidates = new ArrayList<>();
        for (Status status : statusList) {
            Candidate candidate = status.getCandidate();
            if (!candidates.contains(candidate)) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }
    public boolean saveCandidate(Candidate candidate){
        return candidateRepository.save(candidate)!=null;
    }
    public void deleteCandidate(int candidateID){
        Candidate candidate = candidateRepository.findById(candidateID);
        List<Status> statusList = statusRepository.findByCandidate(candidate);
        for(Status status:statusList){
            statusRepository.delete(status);
        }
        candidateRepository.delete(candidate);
    }
    private Status getStatus(int eventID,int candidateID){
        Event event = eventRepository.findById(eventID);
        Candidate candidate = candidateRepository.findById(candidateID);
        return statusRepository.findByEventAndCandidate(event,candidate);
    }
    public void changeCandidateStatus(int eventID,int candidateID){
        Candidate candidate = candidateRepository.findById(candidateID);
        Event event = eventRepository.findById(eventID);
        Status status = statusRepository.findByEventAndCandidate(event,candidate);

    }
    public List<Status> getEventList(int id){
        Candidate candidate = candidateRepository.findById(id);
        return statusRepository.findByCandidate(candidate);
    }
    public void deleteCandidateFromEvent(int eventID,int candidateID){
        Status status = getStatus(eventID,candidateID);
        statusRepository.delete(status);
    }
    public Candidate saveByParams(Map<String,String> params)throws Exception{
        Candidate candidate = candidateRepository.findByNameAndEmail(params.get("name"),params.get("email"));
        Status status = new Status();
        if(candidate!=null){
            throw new Exception("Candidate Already Exist , Try Search For " + candidate.getName());
        }
        else{
            candidate = new Candidate();
            Event event = eventRepository.findByCourseCode(params.get("course"));
            validNewCandidateParams(params);
            List<String> facultyCodeName = utils.facultyResolve(params.get("faculty"));
            System.out.println(facultyCodeName.get(0)+" : " +facultyCodeName.get(1));
            candidate.setName(params.get("name"));
            candidate.setUniversity(universityRepository.findByName(params.get("uv")));
            candidate.setFaculty(facultyRepository.findByCodeAndName(facultyCodeName.get(0),facultyCodeName.get(1)));
            candidate.setGender(params.get("gender"));
            candidate.setEmail(params.get("email"));
            candidate.setAccount(params.get("account"));
            candidate.setPhone(params.get("phone"));
            candidate.setFacebook(params.get("facebook"));
            candidate.setGpa(converter.stringToDouble(params.get("gpa")));
            candidate.setSkill(event.getSkill());
            candidate.setDateOfBirth(converter.stringToDate(params.get("dob"),"MM/dd/yyyy"));
            candidate.setGraduationDate(converter.stringToDate(params.get("graduation"),"MM/dd/yyyy"));
            candidate.setFulltimeDate(converter.stringToDate(params.get("fulltime"),"MM/dd/yyyy"));
            candidateRepository.save(candidate);
            status.setCandidate(candidate);
            status.setEvent(eventRepository.findByCourseCode(params.get("course")));
            status.setStatus(params.get("status"));
            status.setFinalGrade(converter.stringToDouble(params.get("fgrade")));
            status.setCompletionLevel(params.get("clevel"));
            status.setCertificateId(params.get("cerid"));
            statusRepository.save(status);
        }
        return candidate;
    }
    public void editCandidate(Candidate candidate,Map<String,String> params)throws Exception{
        validEditCandidateParams(params);
        candidate.setName(params.get("name"));
        candidate.setEmail(params.get("email"));
        candidate.setAccount(params.get("account"));
        candidate.setUniversity(universityRepository.findByName(params.get("uv")));
        List<String> facultyCodeName = utils.facultyResolve(params.get("faculty"));
        candidate.setFaculty(facultyRepository.findByCodeAndName(facultyCodeName.get(0),facultyCodeName.get(1)));
        candidate.setGender(params.get("gender"));
        candidate.setEmail(params.get("email"));
        candidate.setAccount(params.get("account"));
        candidate.setPhone(params.get("phone"));
        candidate.setFacebook(params.get("facebook"));
        Skill skill = skillRepository.findBySkillName(params.get("skill"));
        candidate.setSkill(skill);
        candidate.setDateOfBirth(converter.stringToDate(params.get("dob"),"MM/dd/yyyy"));
        candidate.setGraduationDate(converter.stringToDate(params.get("graduation"),"MM/dd/yyyy"));
        candidate.setFulltimeDate(converter.stringToDate(params.get("fulltime"),"MM/dd/yyyy"));
        if(params.get("gpa")!=null){
            if(!params.get("gpa").isEmpty()) {
                customValidationService.validGrade(converter.stringToDouble(params.get("gpa")));
            }
        }
        candidateRepository.save(candidate);
    }
    private void validEditCandidateParams(Map<String,String> params)throws Exception{
        List<String> facultyCodeName = utils.facultyResolve(params.get("faculty"));
        University university = universityRepository.findByName(params.get("uv"));
        Faculty faculty = facultyRepository.findByCodeAndName(facultyCodeName.get(0),facultyCodeName.get(1));
        customValidationService.validName(params.get("name"));
        if(university==null){
            throw new Exception("University not found in database");
        }
        if(faculty==null){
            throw new Exception("Faculty not found in database");
        }
        customValidationService.validFaculty(university,faculty);
        customValidationService.validEmail(params.get("email"));
        customValidationService.validSkill(params.get("skill"));
        if(!params.get("phone").isEmpty()){
            customValidationService.validPhoneNumber(params.get("phone"));
        }
        if(params.get("fgrade")!=null){
            if(!params.get("fgrade").isEmpty()) {
                customValidationService.validGrade(converter.stringToDouble(params.get("fgrade")));
            }
        }
        if(!params.get("dob").isEmpty()) {
            customValidationService.validBirthDay(converter.stringToDate(params.get("dob"), "MM/dd/yyyy"));
        }
        if(params.get("gpa")!=null){
            if(!params.get("gpa").isEmpty()) {
                customValidationService.validGrade(converter.stringToDouble(params.get("gpa")));
            }
        }
    }
    private void validNewCandidateParams(Map<String,String> params)throws Exception{
        List<String> facultyCodeName = utils.facultyResolve(params.get("faculty"));
        University university = universityRepository.findByName(params.get("uv"));
        Faculty faculty = facultyRepository.findByCodeAndName(facultyCodeName.get(0),facultyCodeName.get(1));
        customValidationService.validName(params.get("name"));
        if(university==null){
            throw new Exception("University not found in database");
        }
        if(faculty==null){
            throw new Exception("Faculty not found in database");
        }
        customValidationService.validFaculty(university,faculty);
        if(params.get("course").isEmpty()){
            throw new Exception("Please enter a course name to add this candidate to");
        }
        customValidationService.validEmail(params.get("email"));
        if(!params.get("phone").isEmpty()){
            customValidationService.validPhoneNumber(params.get("phone"));
        }
        if(params.get("fgrade")!=null){
            if(!params.get("fgrade").isEmpty()) {
                customValidationService.validGrade(converter.stringToDouble(params.get("fgrade")));
            }
        }
        if(!params.get("dob").isEmpty()) {
            customValidationService.validBirthDay(converter.stringToDate(params.get("dob"), "MM/dd/yyyy"));
        }
        if(params.get("gpa")!=null){
            if(!params.get("gpa").isEmpty()) {
                customValidationService.validGrade(converter.stringToDouble(params.get("gpa")));
            }
        }
    }
}
