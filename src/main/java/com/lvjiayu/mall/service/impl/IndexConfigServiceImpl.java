package com.lvjiayu.mall.service.impl;

import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.dao.IndexConfigMapper;
import com.lvjiayu.mall.entity.IndexConfig;
import com.lvjiayu.mall.service.IndexConfigService;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {
    @Resource
    private IndexConfigMapper indexConfigMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> list = indexConfigMapper.findConfigsPageList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfig(pageUtil);
        PageResult pageResult = new PageResult(list, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        if(indexConfigMapper.insertSelective(indexConfig) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        if(indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId()) == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if(indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long configId) {
        return indexConfigMapper.selectByPrimaryKey(configId);
    }

    @Override
    public boolean deleteBatch(Long[] ids) {
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
