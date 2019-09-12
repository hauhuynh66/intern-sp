package com.intern.controller;

import com.intern.model.*;
import com.intern.repository.*;
import com.intern.utils.Converter;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/manage")
public class FilterController {
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private Converter converter;
    @GetMapping("/getUniversity")
    @ResponseBody
    public List<String> universityList(){
        List<String> universityList = new ArrayList<>();
        List<University> universities = universityRepository.findAll();
        for(University university:universities){
            universityList.add(university.getName());
        }
        return universityList;
    }
    @GetMapping("/getFaculty")
    @ResponseBody
    public List<String> facultyList(){
        List<String> facultyList = new ArrayList<>();
        List<Faculty> faculties = facultyRepository.findAll();
        for(Faculty faculty:faculties){
            facultyList.add(faculty.getCode()+"("+faculty.getName()+")");
        }
        return facultyList;
    }
    @GetMapping("/getSkill")
    @ResponseBody
    public List<String> skillList(){
        List<String> skillList = new ArrayList<>();
        List<Skill> skills = skillRepository.findAll();
        for(Skill skill:skills){
            skillList.add(skill.getSkillName());
        }
        return skillList;
    }
    @GetMapping("/getPrograms")
    @ResponseBody
    public List<String> programList(){
        List<String> programList = new ArrayList<>();
        List<Program> programs = programRepository.findAll();
        for(Program program:programs){
            programList.add(program.getCode());
        }
        return programList;

    }
    @GetMapping("/getEventNames")
    @ResponseBody
    public List<String> eventNameList(){
        List<String> eventList = new ArrayList<>();
        List<Event> events = eventRepository.findAll();
        for(Event event:events){
            eventList.add(event.getCourseCode());
        }
        return eventList;
    }
    @GetMapping("/getUniversityFaculty")
    @ResponseBody
    public List<String> getUniversityInformation(@RequestParam("university")String university){
        List<String> res = new ArrayList<>();
        Set<Faculty> faculties = universityRepository.findByName(university).getFaculties();
        for(Faculty faculty:faculties){
            res.add(faculty.toString());
        }
        return res;
    }
    @GetMapping("/getFacultyUniversity")
    @ResponseBody
    public List<String> getFacultyInformation(@RequestParam("faculty")String faculty){
        List<String> res = new ArrayList<>();
        String fCode = faculty.substring(0,faculty.indexOf('('));
        String fName = faculty.substring(faculty.indexOf('(')+1,faculty.length()-1);
        Faculty f = facultyRepository.findByCodeAndName(fCode,fName);
        Set<University> universities = f.getUniversities();
        for(University u:universities){
            res.add(u.getName());
        }
        return res;
    }
    @GetMapping("/getEventByUniversityAndFaculty")
    @ResponseBody
    public List<String> getEventByUniversityAndFaculty(@RequestParam("university")String university,
                                                       @RequestParam("faculty")String faculty){
        List<String> res = new ArrayList<>();
        University u = universityRepository.findByName(university);
        Set<Faculty> faculties = u.getFaculties();
        Faculty fa = new Faculty();
        for(Faculty f:faculties){
            if(f.toString().equals(faculty)){
                fa = f;
                break;
            }
        }
        List<Event> events = eventRepository.findByUniversityAndFaculty(u,fa);
        for(Event e:events){
            res.add(e.getCourseCode());
        }
        return res;
    }
    @GetMapping("/getSkillByUaF")
    @ResponseBody
    public List<String> getSkillByUniversityAndFaculty(@RequestParam("university") String uName,
                                                       @RequestParam("faculty")String f){
        String fCode = f.substring(0,f.indexOf('('));
        String fName = f.substring(f.indexOf('(')+1,f.length()-1);
        List<Event> events =
                eventRepository.findByUniversityAndFaculty(
                        universityRepository.findByName(uName),
                        facultyRepository.findByCodeAndName(fCode,fName));
        List<String> res = new ArrayList<>();
        for(Event event:events){
            if(!utils.isAvailable(event.getSkill().getSkillName(),res)){
                res.add(event.getSkill().getSkillName());
            }
        }
        return res;
    }
    @GetMapping("/getEventDetail")
    @ResponseBody
    public List<String> function1(@RequestParam("university")String uv,
                                  @RequestParam("faculty")String f,
                                  @RequestParam("skill")String skill){
        List<String> ev = getEventByUniversityAndFaculty(uv,f);
        System.out.println(ev);
        List<String> res = new ArrayList<>();
        for(String e:ev){
            Event event = eventRepository.findByCourseCode(e);
            if(event.getSkill().getSkillName().equals(skill)){
                res.add(event.getCourseCode());
            }
        }
        return res;
    }
    @GetMapping("/getFilterSkill")
    @ResponseBody
    private String getFilterSkill(@RequestParam("code")String code){
        Event event = eventRepository.findByCourseCode(code);
        return event.getSkill().getSkillName();
    }
    /*@GetMapping("/candidates/filter")
    @ResponseBody
    public String filterCandidate(@RequestParam("params")String paramList){
        String response = "";
        List<String> params = Arrays.asList(paramList.split(","));
        System.out.println(params);
        List<Candidate> returnList = new ArrayList<>();
        System.out.println("=====================================================");
        System.out.println("Name Filter");
        if(!params.get(0).equals("")){
            returnList = candidateRepository.findByNameContainingIgnoreCase(params.get(0));
        }else{
            returnList = candidateRepository.findAll();
        }
        System.out.println(returnList.size());
        System.out.println("=====================================================");
        System.out.println("University Filter");
        if(!params.get(1).equals("")){
            filterByUniversity(returnList,params.get(1));
        }
        System.out.println(returnList.size());
        System.out.println("=====================================================");
        System.out.println("Faculty Filter");
        if(!params.get(2).equals("")){
            filterByFaculty(returnList,params.get(2));
        }
        System.out.println(returnList.size());
        System.out.println("=====================================================");
        System.out.println("Skill Filter");
        if(!params.get(3).equals("")){
            filterBySkill(returnList,params.get(3));
        }
        System.out.println(returnList.size());
        System.out.println("=====================================================");
        System.out.println("Event Filter");
        if(!params.get(4).equals("")){
            filterByEvent(returnList,params.get(4));
        }
        if((params.size()>5)&&(!params.get(5).equals(""))&&!(params.get(6).equals(""))){
            System.out.println(converter.stringToDate(params.get(5),"MM/dd/yyyy"));
            System.out.println(converter.stringToDate(params.get(6),"MM/dd/yyyy"));
            filterByDate(returnList,
                    converter.stringToDate(params.get(5),"MM/dd/yyyy"),
                    converter.stringToDate(params.get(6),"MM/dd/yyyy"));
        }
        System.out.println(returnList.size());
        response = utils.candidateMapper(returnList);
        return response;
    }*/
    private List<Candidate> filterByUniversity(List<Candidate> candidates,String universityName){
        if(universityName.equals("--ALL--")){
            return candidates;
        }else{
            University university = universityRepository.findByName(universityName);
            if(university==null){
                return new ArrayList<>();
            }else{
                for(int i=0;i<candidates.size();i++){
                    if(!candidates.get(i).getUniversity().getName().equals(universityName)){
                        candidates.remove(i);
                        i--;
                    }
                }
            }
            return candidates;
        }
    }
    private List<Candidate> filterByFaculty(List<Candidate> candidates,String facultyStr){

        if(facultyStr.equals("--ALL--")){
            return candidates;
        }else{
            String fc = facultyStr.substring(0,facultyStr.indexOf("("));
            String fn = facultyStr.substring(facultyStr.indexOf("(")+1,facultyStr.length()-1);
            Faculty faculty = facultyRepository.findByCodeAndName(fc,fn);
            System.out.println(fc+" : "+fn);
            if(faculty==null){
                return new ArrayList<>();
            }else{
                for(int i=0;i<candidates.size();i++){
                    if((!candidates.get(i).getFaculty().getName().equals(fn))
                            ||(!candidates.get(i).getFaculty().getCode().equals(fc))){
                        candidates.remove(i);
                        i--;
                    }
                }
            }
            return candidates;
        }
    }
    private List<Candidate> filterBySkill(List<Candidate> candidates,String skillname){
        if(skillname=="--ALL--"){
            return candidates;
        }else{
            Skill skill = skillRepository.findBySkillName(skillname);
            if(skill==null){
                return new ArrayList<>();
            }else{
                for(int i=0;i<candidates.size();i++){
                    if(candidates.get(i).getSkill()!=skill){
                        candidates.remove(i);
                        i--;
                    }
                }
            }
            return candidates;
        }
    }
    private List<Candidate> filterByEvent(List<Candidate> candidates,String eventCode){
        if(eventCode.equals("--ALL--")){
            return candidates;
        }else{
            for(int i=0;i<candidates.size();i++){
                boolean isAttend = false;
                Set<Status> statuses = candidates.get(i).getStatuses();
                for(Status status:statuses){
                    if(status.getEvent().getCourseCode().equals(eventCode)){
                        isAttend = true;
                        break;
                    }
                }
                if(!isAttend){
                    candidates.remove(i);
                    i--;
                }
            }
            return candidates;
        }
    }
    private List<Candidate> filterByDate(List<Candidate> candidates, Date fromDate, Date toDate){
        List<Event> filteredEvent = listEventFilter(fromDate,toDate);
        for(int i=0;i<candidates.size();i++){
            boolean isIn = false;
            Set<Status> statuses = candidates.get(i).getStatuses();
            for(Status status:statuses){
                if(!filteredEvent.contains(status.getEvent())){
                    candidates.remove(i);
                    i--;
                    break;
                }
            }
        }
        return candidates;
    }
    public List<Event> listEventFilter(Date fromDate, Date toDate){
        List<Event> events = eventRepository.findAll();
        for(int i=0;i<events.size();i++){
            if(events.get(i).getActualStartDate().before(fromDate)&&events.get(i).getActualEndDate().after(toDate)){
                events.remove(i);
                i--;
            }

        }
        return events;
    }
}
