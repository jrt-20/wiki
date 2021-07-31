package com.futureport.wiki.mapper;

import com.futureport.wiki.entity.Content;
import com.futureport.wiki.entity.ContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper {
    long countByExample(ContentExample example);

    int deleteByExample(ContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Content record);

    int insertSelective(Content record);

    List<Content> selectByExampleWithBLOBs(ContentExample example);

    List<Content> selectByExample(ContentExample example);

    Content selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Content record, @Param("example") ContentExample example);

    int updateByExampleWithBLOBs(@Param("record") Content record, @Param("example") ContentExample example);

    int updateByExample(@Param("record") Content record, @Param("example") ContentExample example);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKeyWithBLOBs(Content record);
}