package com.intern.controller;

import com.intern.model.*;
import com.intern.repository.*;
import com.intern.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Controller
@RequestMapping("/manage/statistics")
public class StatisticController {
    @Autowired
    private MainService service;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping("/dashboard/chart1")
    @ResponseBody
    public Map<String,Integer> getDataChart1(){
        return service.getUniversityStatistics();
    }
    @GetMapping("/getData")
    @ResponseBody
    public List<Object> getData() {
        List<Site> sites = new ArrayList<Site>();
        List<Skill> skills = new ArrayList<Skill>();
        List<Program> programs = new ArrayList<Program>();
        List<Candidate> candidates = new ArrayList<Candidate>();
        List<University> universities = new ArrayList<University>();
        List<Faculty> faculties = new ArrayList<Faculty>();
        try {
            sites = siteRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            skills = skillRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            programs = programRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            candidates = candidateRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            universities = universityRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            faculties = facultyRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        List<Object> data = new ArrayList<>();
        data.add(sites);
        data.add(skills);
        data.add(programs);
        data.add(candidates);
        data.add(universities);
        data.add(faculties);
        return data;
    }
    @GetMapping("/getEvent")
    @ResponseBody
    public List<Map<String,Integer>> getEvent() {
        List<Map<String,Integer>> event = getEventsData();
        return event;
    }

    @GetMapping("/getEventBetweenDate")
    @ResponseBody
    public List<Map<String,Integer>> getEventBetweenDate(@RequestParam String startDate, @RequestParam String endDate) {
        Date filterStartDate = new Date();
        Date filterEndDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            filterStartDate = sdf.parse(startDate);
            filterEndDate = sdf.parse(endDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<Map<String,Integer>> data = new ArrayList<>();
        Map<String,Integer> university = service.UniversityStatisticsOfEventFilter(filterStartDate,filterEndDate);
        Map<String,Integer> skill = service.SkillStatisticsOfEventFilter(filterStartDate,filterEndDate);
        Map<String,Integer> site = service.SiteStatisticsFilter(filterStartDate,filterEndDate);
        Map<String,Integer> program = service.ProgramStatisticsFilter(filterStartDate,filterEndDate);
        data.add(university);
        data.add(skill);
        data.add(site);
        data.add(program);
        return data;
    }

    @GetMapping("/candidates/getData")
    @ResponseBody
    public List<Map<String,Integer>> getCandidatesData(){
        List<Map<String,Integer>> data = new ArrayList<>();
        Map<String,Integer> university =  service.UniversityStatisticsOfCandidate();
        Map<String,Integer> skill = service.SkillStatisticsOfCandidate();
        Map<String,Integer> gender = service.GenderStatisticsOfCandidate();
        data.add(university);
        data.add(skill);
        data.add(gender);
        return data;
    }
    @GetMapping("/events/getData")
    @ResponseBody
    public List<Map<String,Integer>> getEventsData(){
        List<Map<String,Integer>> data = new ArrayList<>();
        Map<String,Integer> university = service.UniversityStatisticsOfEvent();
        Map<String,Integer> skill = service.SkillStatisticsOfEvent();
        Map<String,Integer> site = service.SiteStatistics();
        Map<String,Integer> program = service.ProgramStatistics();
        data.add(university);
        data.add(skill);
        data.add(site);
        data.add(program);
        return data;
    }

    @GetMapping("/filterData")
    @ResponseBody
    public List<List<Map<String,Integer>>> getDataForSelected(){
        List<List<Map<String,Integer>>> data = new ArrayList<>();
        data.add(getEventsData());
        data.add(getCandidatesData());
        return data;
    }
}
