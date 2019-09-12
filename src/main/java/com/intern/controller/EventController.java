package com.intern.controller;

import com.intern.model.Candidate;
import com.intern.model.Event;
import com.intern.model.Status;
import com.intern.model.University;
import com.intern.repository.CandidateRepository;
import com.intern.repository.EventRepository;
import com.intern.repository.StatusRepository;
import com.intern.service.CustomValidationService;
import com.intern.service.EventService;
import com.intern.service.MainService;
import com.intern.utils.Excel;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/manage/events")
public class EventController {
    @Autowired
    private Utils utils;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private MainService mainService;
    @Autowired
    private EventService eventService;
    @Autowired
    private Excel excel;
    @Autowired
    private CustomValidationService validationService;
    @GetMapping("/delete")
    @ResponseBody
    public String deleteEvent(@RequestParam("id")int id)throws Exception{
        Event event = eventRepository.findById(id);
        validationService.validDeleteEvent(event);
        eventService.deleteEvent(event);
        return "Success";
    }
    @GetMapping("/getCandidates")
    @ResponseBody
    public String getCandidate(@RequestParam("code")String code){
        List<Status> statusList = eventService.getCandidateList(code);
        return utils.statusMapper(statusList);
    }
    @GetMapping("/removeCandidate")
    @ResponseBody
    public String removeCandidate(@RequestParam("code")String eventCode,
                                    @RequestParam("id")int candidateId){
        String response = "";
        eventService.deleteCandidateFromEvent(eventCode,candidateId);
        return "Success";
    }
    @GetMapping("/get")
    @ResponseBody
    public List<String> getEvent(@RequestParam("code")String code){
        List<String> response = new ArrayList<>();
        Event event = eventRepository.findByCourseCode(code);
        List<Status> allStatuses = statusRepository.findByEvent(event);
        List<Status> passStatuses = statusRepository.findByEventAndStatus(event,"Passed");
        response.add(allStatuses.size()+"");
        response.add(passStatuses.size()+"");
        response.add(utils.eventMapper(event));
        return response;
    }
    @GetMapping("/export")
    @ResponseBody
    public void createExportFile(@RequestParam("fileName") String fileName,
                                 @RequestParam("event") String eventCode){
        eventCode = eventCode.substring(0,eventCode.length()-1);
        List<String> eventCodes = Arrays.asList(eventCode.split("\\+"));
        List<Event> eventList = new ArrayList<>();
        for(String code:eventCodes){
            Event event  = eventRepository.findByCourseCode(code);
            if(event!=null){
                eventList.add(event);
            }
        }
        System.out.println(eventList.size());
        excel.writeEventList(eventList,fileName);
    }
    @GetMapping("/filter")
    @ResponseBody
    public String eventFilter(@RequestParam(required = false,value = "sites[]")List<String> sites,
                              @RequestParam(required = false,value = "skills[]")List<String> skills,
                              @RequestParam(required = false,value = "statuses[]")List<String> statuses,
                              @RequestParam(required = false,value = "universities[]")List<String> universities,
                              @RequestParam(required = false,value = "programs[]")List<String> programs){
        List<Event> events = eventRepository.findAll();
        List<University> universityList = mainService.lookUpUniversity(universities);
        events = eventService.filterBySites(events,sites);
        events = eventService.filterBySkill(events,skills);
        events = eventService.filterByProgram(events,programs);
        events = eventService.filterByUniversity(events,universityList);
        events = eventService.filterByStatus(events,statuses);
        return utils.eventMapper(events);
    }
    private int getYear(Date d){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.YEAR);
    }
    @GetMapping("/new")
    @ResponseBody
    public String newEvent(@RequestParam("site")String siteName){
        return "OK";
    }
}
