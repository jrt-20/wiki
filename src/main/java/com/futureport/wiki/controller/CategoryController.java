package com.futureport.wiki.controller;

import com.futureport.wiki.req.CategoryQueryReq;
import com.futureport.wiki.req.CategorySaveReq;
import com.futureport.wiki.resp.CommonResp;
import com.futureport.wiki.resp.CategoryQueryResp;
import com.futureport.wiki.resp.PageResp;
import com.futureport.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    public CommonResp Category(@Valid CategoryQueryReq CategoryQueryReq){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> lists =  categoryService.findAll(CategoryQueryReq);
        resp.setContent(lists);
        return resp;
    }
    @RequestMapping("/all")
    public CommonResp all(){
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> lists =  categoryService.all();
        resp.setContent(lists);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq CategorySaveReq){
        CommonResp resp = new CommonResp<>();
        categoryService.save(CategorySaveReq);
        return resp;
    }

    @DeleteMapping ("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id){
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
