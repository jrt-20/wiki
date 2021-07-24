package com.futureport.wiki.controller;

import com.futureport.wiki.req.EbookQueryReq;
import com.futureport.wiki.req.EbookSaveReq;
import com.futureport.wiki.resp.CommonResp;
import com.futureport.wiki.resp.EbookQueryResp;
import com.futureport.wiki.resp.PageResp;
import com.futureport.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @RequestMapping("/list")
    public CommonResp ebook(EbookQueryReq ebookQueryReq){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> lists =  ebookService.findAll(ebookQueryReq);
        resp.setContent(lists);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq ebookSaveReq){
        CommonResp resp = new CommonResp<>();
        ebookService.save(ebookSaveReq);
        return resp;
    }
}
