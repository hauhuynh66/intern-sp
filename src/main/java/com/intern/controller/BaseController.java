package com.intern.controller;

import com.intern.component.CustomMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {
    @Autowired
    private CustomMapper customMapper;
}
