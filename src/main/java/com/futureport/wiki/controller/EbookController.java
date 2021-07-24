package com.futureport.wiki.controller;

import com.futureport.wiki.req.EbookQueryReq;
import com.futureport.wiki.req.EbookSaveReq;
import com.futureport.wiki.resp.CommonResp;
import com.futureport.wiki.resp.EbookQueryResp;
import com.futureport.wiki.resp.PageResp;
import com.futureport.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @RequestMapping("/list")
    public CommonResp ebook(@Valid EbookQueryReq ebookQueryReq){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> lists =  ebookService.findAll(ebookQueryReq);
        resp.setContent(lists);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq ebookSaveReq){
        CommonResp resp = new CommonResp<>();
        ebookService.save(ebookSaveReq);
        return resp;
    }

    @DeleteMapping ("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id){
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
