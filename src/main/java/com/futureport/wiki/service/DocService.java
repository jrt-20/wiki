package com.futureport.wiki.service;

import com.futureport.wiki.entity.Content;
import com.futureport.wiki.entity.Doc;
import com.futureport.wiki.entity.DocExample;
import com.futureport.wiki.except.BusinessException;
import com.futureport.wiki.except.BusinessExceptionCode;
import com.futureport.wiki.mapper.ContentMapper;
import com.futureport.wiki.mapper.DocMapper;
import com.futureport.wiki.mapper.DocMapperCust;
import com.futureport.wiki.req.DocQueryReq;
import com.futureport.wiki.req.DocSaveReq;
import com.futureport.wiki.resp.DocQueryResp;
import com.futureport.wiki.resp.PageResp;
import com.futureport.wiki.utils.CopyUtil;
import com.futureport.wiki.utils.RedisUtil;
import com.futureport.wiki.utils.RequestContext;
import com.futureport.wiki.utils.SnowFlake;
import com.futureport.wiki.websocket.WebSocketServer;
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
    private DocMapperCust docMapperCust;

    @Resource
    private WebSocketServer webSocketServe;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private RedisUtil redisUtil;

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
        Doc doc = CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req,Content.class);
        //更新
        if(!ObjectUtils.isEmpty(req.getId())){
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }else {
            //新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);

            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
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


    public List<DocQueryResp> all(Long ebookId) {
        DocExample example = new DocExample();
        example.createCriteria().andEbookIdEqualTo(ebookId);
        example.setOrderByClause("sort asc");
        List<Doc> list = docMapper.selectByExample(example);
        //列表复制
        List<DocQueryResp> lists = CopyUtil.copyList(list, DocQueryResp.class);
        return lists;

    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        //文档阅读数+1
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        }
        return content.getContent();
    }

    public void vote(Long id) {
        // docMapperCust.increaseVoteCount(id);
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        //推送消息
        Doc docDb = docMapper.selectByPrimaryKey(id);
        webSocketServe.sendInfo("【"+docDb.getName()+"】被点赞!");
    }

    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }


}
