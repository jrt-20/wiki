package com.futureport.wiki.controller;

import com.futureport.wiki.entity.Test;
import com.futureport.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloWorld {

    @Resource
    private TestService testService;

    @Value("${test.hello}")
    private String test;

    @RequestMapping("/hello")
    public String hello(){
        String str = "hello,world"+test;
        return str;
    }

    @RequestMapping("/mybatis")
    public Test mybatis(){
        return testService.list();
    }
}
