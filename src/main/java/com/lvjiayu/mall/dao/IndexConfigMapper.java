package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.IndexConfig;
import com.lvjiayu.mall.util.PageQueryUtil;

import java.util.List;

public interface IndexConfigMapper {

    List<IndexConfig> findConfigsPageList(PageQueryUtil pageUtil);

    int getTotalIndexConfig(PageQueryUtil pageUtil);

    int insertSelective(IndexConfig indexConfig);

    IndexConfig selectByPrimaryKey(Long configId);

    int updateByPrimaryKeySelective(IndexConfig indexConfig);

    int deleteBatch(Long[] ids);
}
