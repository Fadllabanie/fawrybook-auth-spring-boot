package com.Fawrybook.Fawrybook.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String welcome(){
        return "Welcome to Fawrybook Api from auth server ";
    }
    
}
