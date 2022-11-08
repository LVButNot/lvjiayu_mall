package com.lvjiayu.mall.service;

import com.lvjiayu.mall.entity.IndexConfig;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;

public interface IndexConfigService {

    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long configId);

    boolean deleteBatch(Long[] ids);
}
