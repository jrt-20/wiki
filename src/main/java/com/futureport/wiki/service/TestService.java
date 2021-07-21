package com.futureport.wiki.service;

import com.futureport.wiki.entity.Test;
import com.futureport.wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    public Test list(){
        return testMapper.selectByPrimaryKey(1);
    }
}
