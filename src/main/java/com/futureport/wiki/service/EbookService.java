package com.futureport.wiki.service;

import com.futureport.wiki.entity.Ebook;
import com.futureport.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> findAll(){
        return ebookMapper.selectByExample(null);
    }
}
