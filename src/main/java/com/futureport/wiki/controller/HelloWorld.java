package com.futureport.wiki.controller;

import com.futureport.wiki.entity.Demo;
import com.futureport.wiki.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HelloWorld {

    @Resource
    private DemoService demoService;

    @Value("${test.hello}")
    private String test;

    @RequestMapping("/hello")
    public String hello(){
        String str = "hello,world"+test;
        return str;
    }

    @RequestMapping("/mybatis")
    public List<Demo> mybatis(){
        return demoService.list();
    }
}
