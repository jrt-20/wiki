package com.futureport.wiki.controller;

import com.futureport.wiki.entity.Ebook;
import com.futureport.wiki.resp.CommonResp;
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
    public CommonResp ebook(){
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> lists =  ebookService.findAll();
        resp.setContent(lists);
        return resp;
    }
}
