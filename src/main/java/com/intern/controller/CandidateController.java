package com.intern.controller;

import com.intern.model.*;
import com.intern.repository.*;
import com.intern.service.CandidateService;
import com.intern.service.CustomValidationService;
import com.intern.service.MainService;
import com.intern.utils.Converter;
import com.intern.utils.Excel;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Controller
@RequestMapping("/manage/candidates")
public class CandidateController {
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private Excel excel;
    @Autowired
    private Converter converter;
    @Autowired
    private MainService mainService;
    @Autowired
    private CustomValidationService validationService;
    @Autowired
    private Utils utils;
    @GetMapping("/delete")
    @ResponseBody
    public String deleteCandidate(@RequestParam("id")int id){
        candidateService.deleteCandidate(id);
        return "Success";
    }

    @GetMapping("/new")
    @ResponseBody
    public List<String> newCandidate(@RequestParam Map<String,String> params)throws Exception{
        List<String> response = new ArrayList<>();
        Candidate candidate = candidateService.saveByParams(params);
        response.add("Success Add Candidate");
        response.add(utils.candidateMapper(candidate));
        return response;
    }
    @GetMapping("/changestatus")
    @ResponseBody
    public String changeCandidateEventStatus(@RequestParam("event") int eventid,
                                             @RequestParam("candidate")int candidateid,
                                             @RequestParam("status")String s,
                                             @RequestParam("grade")String fGrade,
                                             @RequestParam("clevel")String clevel,
                                             @RequestParam("cerid")String cerid)throws Exception{
        String response = "";
        response = "Success";
        Status status = statusRepository.findByEventAndCandidate(
                eventRepository.findById(eventid),
                candidateRepository.findById(candidateid));
        status.setStatus(s);
        status.setFinalGrade(converter.stringToDouble(fGrade));
        status.setCompletionLevel(clevel);
        status.setCertificateId(cerid);
        statusRepository.save(status);

        return response;
    }
    @GetMapping("/get")
    @ResponseBody
    public String getCandidate(@RequestParam("id")int id){
        String response = "";
        Candidate candidate = candidateRepository.findById(id);
        response = utils.candidateMapper(candidate);
        return response;
    }
    @GetMapping("/getEvents")
    @ResponseBody
    public String getEventsAttend(@RequestParam("id")int id){
        List<Status> statusList = candidateService.getEventList(id);
        return utils.statusMapper(statusList);
    }
    @GetMapping("/export")
    @ResponseBody
    public void createExportFile(@RequestParam("file") String fileName,
                                 @RequestParam("candidate") String candidates){
        candidates = candidates.substring(0,candidates.length()-1);
        List<String> id = Arrays.asList(candidates.split("\\+"));
        List<Candidate> candidateList = new ArrayList<>();
        for(String c:id){
            Candidate candidate = candidateRepository.findById(converter.stringToInt(c));
            if(candidate!=null){
                candidateList.add(candidate);
            }
        }
        System.out.println(candidateList.size());
        excel.writeCandidateList(candidateList,fileName);
    }
    @GetMapping("/edit")
    @ResponseBody
    public String editCandidate(@RequestParam Map<String,String> params)throws Exception{
        Candidate candidate = candidateRepository.findById(Integer.parseInt(params.get("id")));
        candidateService.editCandidate(candidate,params);
        return "Successfully edit candidate";
    }
    @GetMapping("/remove")
    @ResponseBody
    private String removeCandidateFromEvent(@RequestParam("candidate")int candidateID,
                                            @RequestParam("event")int eventID){
        candidateService.deleteCandidateFromEvent(eventID,candidateID);
        return "Success";
    }
    @GetMapping("/getEventDate")
    @ResponseBody
    public List<List<String>> getEventDate(){
        List<List<String>> eventList= new ArrayList<>();

        List<Event> events = eventRepository.findAll();
        for(int i=0;i<events.size();i++){
            List<String> e = new ArrayList<>();
            e.add(events.get(i).getPlannedStartDate()+"");
            e.add(events.get(i).getPlannedEndDate()+"");
            e.add(events.get(i).getCourseCode());
            eventList.add(e);
        }
        return eventList;
    }
    @GetMapping("/filter")
    @ResponseBody
    public String filter(@RequestParam(required = false,value = "universities[]")List<String> universities,
                         @RequestParam(required = false,value = "skills[]")List<String> skillNames,
                         @RequestParam(required = false,value = "statuses[]")List<String> status,
                         @RequestParam(required = false,value = "events[]")List<String> events){
        List<University> universityList = mainService.lookUpUniversity(universities);
        List<Status> statusList = candidateService.filterByUniversity(statusRepository.findAll(),universityList);
        statusList = candidateService.filterBySkill(statusList,skillNames);
        statusList = candidateService.filterByStatus(statusList,status);
        statusList = candidateService.filterByEvent(statusList,events);
        List<Candidate> candidates = candidateService.lookupCandidate(statusList);
        return utils.candidateMapper(candidates);
    }
}
