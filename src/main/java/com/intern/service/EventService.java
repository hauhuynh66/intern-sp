package com.intern.service;

import com.intern.model.Candidate;
import com.intern.model.Event;
import com.intern.model.Status;
import com.intern.model.University;
import com.intern.repository.CandidateRepository;
import com.intern.repository.EventRepository;
import com.intern.repository.StatusRepository;
import org.apache.poi.ss.formula.functions.Even;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    public List<Event> filterBySites(List<Event> events, List<String> siteName){
        if(siteName==null){
            return events;
        }
        for(int i=0;i<events.size();i++){
            boolean isContains = false;
            for(String site:siteName){
                if(events.get(i).getSite().getSite().equals(site)){
                    isContains = true;
                    break;
                }
            }
            if(!isContains){
                events.remove(i);
                i--;
            }
        }
        return events;
    }
    public List<Event> filterBySkill(List<Event> events,List<String> skillName){
        if(skillName==null){
            return events;
        }
        for(int i=0;i<events.size();i++){
            boolean isContains = false;
            for(String skill:skillName){
                if(events.get(i).getSkill().getSkillName().equals(skill)){
                    isContains = true;
                    break;
                }
            }
            if(!isContains){
                events.remove(i);
                i--;
            }
        }
        return events;
    }
    public List<Event> filterByProgram(List<Event> events,List<String> programCodes){
        if(programCodes==null){
            return events;
        }
        for(int i=0;i<events.size();i++){
            boolean isContains = false;
            for(String code:programCodes){
                if(events.get(i).getProgram().getCode().equals(code)){
                    isContains=true;
                    break;
                }
            }
            if(!isContains){
                events.remove(i);
                i--;
            }
        }
        return events;
    }
    private List<Event> filterInProgressEvent(List<Event> events){
        Date date = new Date();
        for(int i=0;i<events.size();i++){
            if(events.get(i).getActualStartDate().before(date)&& events.get(i).getActualEndDate().after(date)){
                System.out.println("In Progress");
            }else {
                events.remove(i);
                i--;
            }
        }
        return events;
    }
    private List<Event> filterDoneEvents(List<Event> events){
        Date date = new Date();
        for(int i=0;i<events.size();i++){
            if(events.get(i).getActualEndDate().after(date)){
                events.remove(i);
                i--;
            }
        }
        return events;
    }
    private List<Event> filterPlanningEvents(List<Event> events){
        Date date = new Date();
        for(int i=0;i<events.size();i++){
            if(events.get(i).getActualStartDate().before(date)){
                events.remove(i);
                i--;
            }
        }
        return events;
    }
    public List<Event> filterByStatus(List<Event> events,List<String> params){
        List<Event> finalList = new ArrayList<>();
        if(params==null){
            return events;
        }
        if(params.contains("In Progress")){
            addToEventList(finalList,filterInProgressEvent(events));
        }
        if(params.contains("Done")){
            addToEventList(finalList,filterDoneEvents(events));
        }
        if(params.contains("Planning")){
            addToEventList(finalList,filterPlanningEvents(events));
        }
        return finalList;
    }
    private void addToEventList(List<Event> events,List<Event> e){
        for (Event event : e) {
            if (!events.contains(event)) {
                events.add(event);
            }
        }
    }
    public List<Event> filterByUniversity(List<Event> events, List<University> universities){
        if(universities==null){
            return events;
        }
        for(int i=0;i<events.size();i++){
            boolean isContains = false;
            for(University university:universities){
                if(events.get(i).getUniversity()==university){
                    isContains = true;
                    break;
                }
            }
            if(!isContains){
                events.remove(i);
                i--;
            }
        }
        return events;
    }
    public void deleteEvent(Event event){
        List<Status> statusList = statusRepository.findByEvent(event);
        for(Status status:statusList){
            statusRepository.delete(status);
        }
        eventRepository.delete(event);
    }
    public List<Status> getCandidateList(String eventCode){
        Event event = eventRepository.findByCourseCode(eventCode);
        return statusRepository.findByEvent(event);
    }
    public void deleteCandidateFromEvent(String eventCode,int candidateId){
        Event event = eventRepository.findByCourseCode(eventCode);
        Candidate candidate = candidateRepository.findById(candidateId);
        Status status = statusRepository.findByEventAndCandidate(event,candidate);
        statusRepository.delete(status);
    }
}
