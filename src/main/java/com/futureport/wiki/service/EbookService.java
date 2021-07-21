package com.futureport.wiki.service;

import com.futureport.wiki.entity.Ebook;
import com.futureport.wiki.entity.EbookExample;
import com.futureport.wiki.mapper.EbookMapper;
import com.futureport.wiki.req.EbookReq;
import com.futureport.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> findAll(EbookReq req){
        EbookExample example = new EbookExample();
        EbookExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> list = ebookMapper.selectByExample(example);

        List<EbookResp> lists = new LinkedList<>();
        /**
         * 遍历Ebook集合，转成EbookResp
         */
        for (Ebook ebook : list) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            lists.add(ebookResp);
        }
        return lists;
    }
}
