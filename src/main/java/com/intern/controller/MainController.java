package com.intern.controller;

import com.intern.repository.*;
import com.intern.service.MainService;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manage")
public class MainController {
    @Autowired
    private Utils utils;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private MainService service;
    @GetMapping("/dashboard")
    @ResponseBody
    public ModelAndView dashboard(){
        ModelAndView mv = new ModelAndView("dashboard");
        mv.addObject("admin",utils.getCurrentAdminName());
        mv.addObject("events",service.getNumberOfEvents());
        mv.addObject("candidates",service.getNumberOfCandidates());
        mv.addObject("partners",service.getNumberOfCurrentPartners());
        mv.addObject("sites",service.getNumberOfSites());
        mv.addObject("skills",service.getNumberOfSkills());
        return mv;
    }
    @GetMapping("/events")
    @ResponseBody
    public ModelAndView events(){
        ModelAndView mv = new ModelAndView("events");
        mv.addObject("admin",utils.getCurrentAdminName());
        mv.addObject("events",eventRepository.findAll());
        return mv;
    }
    @GetMapping("/candidates")
    @ResponseBody
    public ModelAndView candidates(){
        ModelAndView mv = new ModelAndView("candidates");
        mv.addObject("admin",utils.getCurrentAdminName());
        mv.addObject("candidates",candidateRepository.findAll());
        return mv;
    }
    @GetMapping("/statistics")
    @ResponseBody
    public ModelAndView statistics(){
        ModelAndView mv = new ModelAndView("statistics");
        mv.addObject("admin",utils.getCurrentAdminName());
        return mv;
    }
}
