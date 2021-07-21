package com.futureport.wiki.controller;

import com.futureport.wiki.entity.Ebook;
import com.futureport.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @RequestMapping("/list")
    public List<Ebook> ebook(){
        return ebookService.findAll();
    }
}
