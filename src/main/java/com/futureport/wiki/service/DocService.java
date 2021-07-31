package com.futureport.wiki.service;

import com.futureport.wiki.entity.Doc;
import com.futureport.wiki.entity.DocExample;
import com.futureport.wiki.mapper.DocMapper;
import com.futureport.wiki.req.DocQueryReq;
import com.futureport.wiki.req.DocSaveReq;
import com.futureport.wiki.resp.DocQueryResp;
import com.futureport.wiki.resp.PageResp;
import com.futureport.wiki.utils.CopyUtil;
import com.futureport.wiki.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> findAll(DocQueryReq req){


        DocExample example = new DocExample();
        example.setOrderByClause("sort asc");
        DocExample.Criteria criteria = example.createCriteria();

//        if(!ObjectUtils.isEmpty(req.getName())){
//            criteria.andNameLike("%"+req.getName()+"%");
//        }
        /**
         * 开启分页
         */
        LOG.info("req:{}",req.getPage());
        LOG.info("req:{}",req.getSize());

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> list = docMapper.selectByExample(example);
        PageInfo<Doc> pageInfo = new PageInfo<>(list);

        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());
//        List<DocResp> lists = new LinkedList<>();
//        /**
//         * 遍历Doc集合，转成DocResp
//         */
//        for (Doc Doc : list) {
////            DocResp DocResp = new DocResp();
////            BeanUtils.copyProperties(Doc,DocResp);
              //对象复制
//            DocResp DocResp = CopyUtil.copy(Doc,DocResp.class);
//            lists.add(DocResp);
//        }
//        return lists;


        //列表复制
        List<DocQueryResp> lists = CopyUtil.copyList(list, DocQueryResp.class);

        //返回新的带分页参数的对象
        PageResp<DocQueryResp> PageResp = new PageResp<DocQueryResp>();

        PageResp.setTotal(pageInfo.getTotal());
        PageResp.setList(lists);

        return PageResp;
    }

    /**
     * 保存
     */
    public void save(DocSaveReq req){
        //单体复制
        Doc Doc = CopyUtil.copy(req,Doc.class);
        if(!ObjectUtils.isEmpty(req.getId())){
            docMapper.updateByPrimaryKey(Doc);
        }else {
            Doc.setId(snowFlake.nextId());
            docMapper.insert(Doc);
        }

    }

    /**
     * 删除
     */
    public void delete(long id){
        docMapper.deleteByPrimaryKey(id);

    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }


    public List<DocQueryResp> all(){


        DocExample example = new DocExample();
        example.setOrderByClause("sort asc");
        List<Doc> list = docMapper.selectByExample(example);

        //列表复制
        List<DocQueryResp> lists = CopyUtil.copyList(list, DocQueryResp.class);
        return lists;

    }
}
