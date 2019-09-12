package com.intern.service;

import com.intern.model.*;
import com.intern.repository.*;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private Utils utils;
    public int getNumberOfEvents(){
        List<Event> events = eventRepository.findAll();
        return events.size();
    }
    public int getNumberOfCandidates(){
        List<Candidate> candidates = candidateRepository.findAll();
        return candidates.size();
    }
    public int getNumberOfCurrentPartners(){
        return getListOfUniversityCode().size();
    }
    public int getNumberOfSites(){
        return getListOfSites().size();
    }
    public int getNumberOfSkills(){
        return getListOfSkills().size();
    }
    public int getNumberOfCandidatesByUniversity(String universityCode){
        int number = 0;
        List<Event> events = eventRepository.findAll();
        List<Candidate> candidates = new ArrayList<>();
        for (Event event : events) {
            University university = event.getUniversity();
            if (university.getCode().equals(universityCode)) {
                List<Status> statusList = statusRepository.findByEvent(event);
                for (Status status : statusList) {
                    if (!candidates.contains(status.getCandidate())) {
                        candidates.add(status.getCandidate());
                    }
                }
            }
        }
        return candidates.size();
    }
    public Map<String,Integer> getUniversityStatistics(){
        List<String> codeList = getListOfUniversityCode();
        Map<String,Integer> data = new HashMap<>();
        for(int i=0;i<codeList.size();i++){
            int number = getNumberOfCandidatesByUniversity(codeList.get(i));
            data.put(codeList.get(i),number);
        }
        return data;
    }
    private List<String> getListOfUniversityCode(){
        List<Event> events = eventRepository.findAll();
        if(events.size()==0){
            return new ArrayList<>();
        }else {
            List<String> codeList = new ArrayList<>();
            codeList.add(events.get(0).getUniversity().getCode());
            for(int i=0;i<events.size();i++){
                if(!utils.isAvailable(events.get(i).getUniversity().getCode(),codeList)){
                    codeList.add(events.get(i).getUniversity().getCode());
                }
            }
            return codeList;
        }
    }
    private List<String> getListOfSites(){
        List<Event> events = eventRepository.findAll();
        if(events.size()==0){
            return new ArrayList<>();
        }else{
            List<String> sites = new ArrayList<>();
            sites.add(events.get(0).getSite().getSite());
            for(int i=0;i<events.size();i++){
                if(!utils.isAvailable(events.get(i).getSite().getSite(),sites)){
                    sites.add(events.get(i).getSite().getSite());
                }
            }
            return sites;
        }
    }
    private List<String> getListOfSkills(){
        List<Event> events = eventRepository.findAll();
        if(events.size()==0){
            return new ArrayList<>();
        }else{
            List<String> skills = new ArrayList<>();
            skills.add(events.get(0).getSkill().getSkillName());
            for(int i=0;i<events.size();i++){
                if(!utils.isAvailable(events.get(i).getSkill().getSkillName(),skills)){
                    skills.add(events.get(i).getSkill().getSkillName());
                }
            }
            return skills;
        }
    }
    public List<University> lookUpUniversity(List<String> universitiesName){
        if(universitiesName==null){
            return universityRepository.findAll();
        }
        List<University> universities = new ArrayList<>();
        for(String u:universitiesName){
            University university = universityRepository.findByName(u);
            universities.add(university);
        }
        return universities;
    }
    private List<String> getListOfSkillName(){
        List<String> list = new ArrayList<String>();
        List<Skill> skills = skillRepository.findAll();
        skills.forEach(item -> {
            list.add(item.getSkillName());
        });
        return list;
    }
    private List<String> getListOfSiteName(){
        List<String> list = new ArrayList<String>();
        List<Site> sites = siteRepository.findAll();
        sites.forEach(item -> {
            list.add(item.getSite());
        });
        return list;
    }
    private List<String> getListOfProgramCodeName(){
        List<String> list = new ArrayList<String>();
        List<Program> programs = programRepository.findAll();
        programs.forEach(item -> {
            list.add(item.getCode());
        });
        return list;
    }
    private List<String> getListOfUniversityCodeName(){
        List<String> list = new ArrayList<String>();
        List<University> universities = universityRepository.findAll();
        universities.forEach(item -> {
            //String str = item.getCode();
            //str = str.substring(str.indexOf(".")+1);
            list.add(item.getCode());
        });
        return list;
    }
    private List<String> getListOfFacultyCodeName(){
        List<String> list = new ArrayList<String>();
        List<Faculty> faculties = facultyRepository.findAll();
        faculties.forEach(item -> {
            list.add(item.getCode());
        });
        return list;
    }
    //Candidate Skill
    private int countNumberOfSkillCodeInCandidate(String skillCode, List<Candidate> candidates) {
        int count = 0;
        for(int i=0;i<candidates.size();i++){
            Skill skill = candidates.get(i).getSkill();
            if(skill.getSkillName().equals(skillCode)){
                count += 1;
            }
        }
        return count;
    }

    private Map<String,Integer> getNumberEachSkillOfCandidate(List<Candidate> candidates){
        List<String> skillList = getListOfSkillName();
        Map<String,Integer> data = new HashMap<>();
        for(int i=0;i<skillList.size();i++){
            int number = countNumberOfSkillCodeInCandidate(skillList.get(i),candidates);
            data.put(skillList.get(i),number);
        }
        return data;
    }
    //Candidate University
    private int countNumberOfUniversityCodeInCandidate(String universityCode, List<Candidate> candidates ) {
        int count = 0;
        for(int i=0;i<candidates.size();i++){
            University university = candidates.get(i).getUniversity();
            if(university.getCode().equals(universityCode)){
                count += 1;
            }
        }
        return count;
    }

    private Map<String,Integer> getNumberEachUniversityOfCandidate(List<Candidate> candidates){
        List<String> univerList = getListOfUniversityCodeName();
        Map<String,Integer> data = new HashMap<>();
        for(int i=0;i<univerList.size();i++){
            int number = countNumberOfUniversityCodeInCandidate(univerList.get(i),candidates);
            data.put(univerList.get(i),number);
        }
        return data;
    }
    //Event Skill
    private int countNumberOfSkillCodeInEvent(String skillCode,List<Event> events) {
        int count = 0;
        for(int i=0;i<events.size();i++){
            Skill skill = events.get(i).getSkill();
            if(skill.getSkillName().equals(skillCode)){
                count += 1;
            }
        }
        return count;
    }

    private Map<String,Integer> getNumberEachSkillOfEvent(List<Event> events){
        List<String> skillList = getListOfSkillName();
        Map<String,Integer> data = new HashMap<>();
        for(int i=0;i<skillList.size();i++){
            int number = countNumberOfSkillCodeInEvent(skillList.get(i),events);
            data.put(skillList.get(i),number);
        }
        return data;
    }

    //Event Site
    private int countNumberOfSiteCodeInEvent(String siteCode,List<Event> events) {
        int count = 0;
        for(int i=0;i<events.size();i++){
            Site site = events.get(i).getSite();
            if(site.getSite().equals(siteCode)){
                count += 1;
            }
        }
        return count;
    }

    private Map<String,Integer> getNumberEachSiteOfEvent(List<Event> events){
        List<String> siteList = getListOfSiteName();
        Map<String,Integer> data = new HashMap<>();
        for(int i=0;i<siteList.size();i++){
            int number = countNumberOfSiteCodeInEvent(siteList.get(i),events);
            data.put(siteList.get(i),number);
        }
        return data;
    }
    //Event Program
    private int countNumberOfProgramCodeInEvent(String programCode,List<Event> events) {
        int count = 0;
        for(int i=0;i<events.size();i++){
            Program program = events.get(i).getProgram();
            if(program.getCode().equals(programCode)){
                count += 1;
            }
        }
        return count;
    }

    private Map<String,Integer> getNumberEachProgramOfEvent(List<Event> events){
        List<String> programList = getListOfProgramCodeName();
        Map<String,Integer> data = new HashMap<>();
        for(int i=0;i<programList.size();i++){
            int number = countNumberOfProgramCodeInEvent(programList.get(i),events);
            data.put(programList.get(i),number);
        }
        return data;
    }
    //Event University
    private int countNumberOfUniversityCodeInEvent(String universityCode,List<Event> events) {
        int count = 0;
        for(int i=0;i<events.size();i++){
            University university = events.get(i).getUniversity();
            if(university.getCode().equals(universityCode)){
                count += 1;
            }
        }
        return count;
    }

    private Map<String,Integer> getNumberEachUniversityOfEvent(List<Event> events){
        List<String> univerList = getListOfUniversityCodeName();
        Map<String,Integer> data = new HashMap<>();
        for(int i=0;i<univerList.size();i++){
            int number = countNumberOfUniversityCodeInEvent(univerList.get(i),events);
            data.put(univerList.get(i),number);
        }
        return data;
    }


    private int countNumberOfGenderOfCandidate(String gender,List<Candidate> candidates) {
        int count = 0;
        for(int i=0;i<candidates.size();i++){
            if(candidates.get(i).getGender().equals(gender)){
                count += 1;
            }
        }
        return count;
    }

    private Map<String,Integer> getNumberEachGenderOfCandidate(List<Candidate> candidates){
        List<String> genderList = new ArrayList<>();
        Map<String,Integer> data = new HashMap<>();
        candidates.forEach(item -> {
            boolean check = genderList.contains(item.getGender());
            if(check) return;
            else {
                genderList.add(item.getGender());
            }
        });
        for(int i=0;i<genderList.size();i++){
            int number = countNumberOfGenderOfCandidate(genderList.get(i),candidates);
            data.put(genderList.get(i),number);
        }
        return data;
    }

    public Map<String,Integer> GenderStatisticsOfCandidate(){
        List<Candidate> candidates = candidateRepository.findAll();
        return getNumberEachGenderOfCandidate(candidates);
    }

    //Event public function
    public Map<String,Integer> UniversityStatisticsOfEvent(){
        List<Event> events = eventRepository.findAll();
        return getNumberEachUniversityOfEvent(events);
    }
    public Map<String,Integer> SkillStatisticsOfEvent(){
        List<Event> events = eventRepository.findAll();
        return getNumberEachSkillOfEvent(events);
    }
    public Map<String,Integer> SiteStatistics(){
        List<Event> events = eventRepository.findAll();
        return getNumberEachSiteOfEvent(events);
    }
    public Map<String,Integer> ProgramStatistics(){
        List<Event> events = eventRepository.findAll();
        return getNumberEachProgramOfEvent(events);
    }

    //Candidate public function
    public Map<String,Integer> UniversityStatisticsOfCandidate(){
        List<Candidate> candidates = candidateRepository.findAll();
        return getNumberEachUniversityOfCandidate(candidates);
    }
    public Map<String,Integer> SkillStatisticsOfCandidate(){
        List<Candidate> candidates = candidateRepository.findAll();
        return getNumberEachSkillOfCandidate(candidates);
    }
    //Event filter
    //Event public function
    public Map<String,Integer> UniversityStatisticsOfEventFilter(Date startDate, Date endDate){
        List<Event> events = eventRepository.findByStartDateAndEndDate(startDate, endDate);
        return getNumberEachUniversityOfEvent(events);
    }
    public Map<String,Integer> SkillStatisticsOfEventFilter(Date startDate, Date endDate){
        List<Event> events = eventRepository.findByStartDateAndEndDate(startDate, endDate);
        return getNumberEachSkillOfEvent(events);
    }
    public Map<String,Integer> SiteStatisticsFilter(Date startDate, Date endDate){
        List<Event> events = eventRepository.findByStartDateAndEndDate(startDate, endDate);
        return getNumberEachSiteOfEvent(events);
    }
    public Map<String,Integer> ProgramStatisticsFilter(Date startDate, Date endDate){
        List<Event> events = eventRepository.findByStartDateAndEndDate(startDate, endDate);
        return getNumberEachProgramOfEvent(events);
    }

    //Candidate public function
    public Map<String,Integer> UniversityStatisticsOfCandidateFilter(){
        List<Candidate> candidates = candidateRepository.findAll();
        return getNumberEachUniversityOfCandidate(candidates);
    }
    public Map<String,Integer> SkillStatisticsOfCandidateFilter(){
        List<Candidate> candidates = candidateRepository.findAll();
        return getNumberEachSkillOfCandidate(candidates);
    }

}
