package com.futureport.wiki.controller;

import com.futureport.wiki.req.EbookReq;
import com.futureport.wiki.resp.CommonResp;
import com.futureport.wiki.resp.EbookResp;
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
    public CommonResp ebook(EbookReq ebookReq){
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> lists =  ebookService.findAll(ebookReq);
        resp.setContent(lists);
        return resp;
    }
}
