package com.futureport.wiki.controller;

import com.futureport.wiki.req.DocQueryReq;
import com.futureport.wiki.req.DocSaveReq;
import com.futureport.wiki.resp.DocQueryResp;
import com.futureport.wiki.resp.CommonResp;
import com.futureport.wiki.resp.PageResp;
import com.futureport.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @RequestMapping("/list")
    public CommonResp Doc(@Valid DocQueryReq DocQueryReq){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> lists =  docService.findAll(DocQueryReq);
        resp.setContent(lists);
        return resp;
    }

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq DocSaveReq){
        CommonResp resp = new CommonResp<>();
        docService.save(DocSaveReq);
        return resp;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        docService.vote(id);
        return resp;
    }
}
