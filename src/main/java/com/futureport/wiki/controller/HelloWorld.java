package com.futureport.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @Value("${test.hello}")
    private String test;

    @RequestMapping("/hello")
    public String hello(){
        String str = "hello,world"+test;
        return str;
    }
}
