package com.intern.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping({"/","/login"})
    @ResponseBody
    public ModelAndView home(){
        return new ModelAndView("login");
    }
    @GetMapping({"/register"})
    @ResponseBody
    public ModelAndView register(){
        return new ModelAndView(("register"));
    }
    @GetMapping("/forbidden")
    @ResponseBody
    public ModelAndView forbidden(){
        return new ModelAndView("forbidden");
    }
}
