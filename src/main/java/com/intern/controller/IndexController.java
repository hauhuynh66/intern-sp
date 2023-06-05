package com.intern.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping({"/","/login"})
    public String home(){
        return "login";
    }
    @GetMapping({"/register"})
    public String register(){
        return "register";
    }
    @GetMapping("/forbidden")
    public String forbidden(){
        return "forbidden";
    }
}
