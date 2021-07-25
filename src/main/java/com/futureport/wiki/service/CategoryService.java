package com.futureport.wiki.service;

import com.futureport.wiki.entity.Category;
import com.futureport.wiki.entity.CategoryExample;
import com.futureport.wiki.mapper.CategoryMapper;
import com.futureport.wiki.req.CategoryQueryReq;
import com.futureport.wiki.req.CategorySaveReq;
import com.futureport.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> findAll(CategoryQueryReq req){


        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();

//        if(!ObjectUtils.isEmpty(req.getName())){
//            criteria.andNameLike("%"+req.getName()+"%");
//        }
        /**
         * 开启分页
         */
        LOG.info("req:{}",req.getPage());
        LOG.info("req:{}",req.getSize());

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> list = categoryMapper.selectByExample(example);
        PageInfo<Category> pageInfo = new PageInfo<>(list);

        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());
//        List<CategoryResp> lists = new LinkedList<>();
//        /**
//         * 遍历Category集合，转成CategoryResp
//         */
//        for (Category Category : list) {
////            CategoryResp CategoryResp = new CategoryResp();
////            BeanUtils.copyProperties(Category,CategoryResp);
              //对象复制
//            CategoryResp CategoryResp = CopyUtil.copy(Category,CategoryResp.class);
//            lists.add(CategoryResp);
//        }
//        return lists;


        //列表复制
        List<CategoryQueryResp> lists = CopyUtil.copyList(list, CategoryQueryResp.class);

        //返回新的带分页参数的对象
        PageResp<CategoryQueryResp> PageResp = new PageResp<CategoryQueryResp>();

        PageResp.setTotal(pageInfo.getTotal());
        PageResp.setList(lists);

        return PageResp;
    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req){
        //单体复制
        Category Category = CopyUtil.copy(req,Category.class);
        if(!ObjectUtils.isEmpty(req.getId())){
            categoryMapper.updateByPrimaryKey(Category);
        }else {
            Category.setId(snowFlake.nextId());
            categoryMapper.insert(Category);
        }

    }

    /**
     * 删除
     */
    public void delete(long id){
        categoryMapper.deleteByPrimaryKey(id);

    }
}
