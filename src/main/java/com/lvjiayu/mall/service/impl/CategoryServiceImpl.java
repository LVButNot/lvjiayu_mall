package com.lvjiayu.mall.service.impl;

import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.dao.CategoryMapper;
import com.lvjiayu.mall.entity.GoodsCategory;
import com.lvjiayu.mall.service.CategoryService;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public PageResult getCategoriesPage(PageQueryUtil pageUtil) {
        List<GoodsCategory> goodsCategoryList = categoryMapper.findGoodsCategoryList(pageUtil);
        int total = categoryMapper.getTotalGoodsCategories(pageUtil);
        PageResult pageResult = new PageResult(goodsCategoryList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public GoodsCategory getGoodsCategoryById(Long categoryId) {
        GoodsCategory category = categoryMapper.selectByPrimaryKey(categoryId);
        return category;
    }

    @Override
    public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
        return categoryMapper.selectByLevelAndParentIdsAndNumber(parentIds, categoryLevel, 0);//0代表查询所有
    }

    @Override
    public String saveCategory(GoodsCategory goodsCategory) {
        //需要先查询一下是否有重复
        GoodsCategory temp = categoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if(temp != null){
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if(categoryMapper.insertSelective(goodsCategory) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateGoodsCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = categoryMapper.selectByPrimaryKey(goodsCategory.getCategoryId());
        if(temp == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        GoodsCategory temp2 = categoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if(temp2 != null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())){
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        goodsCategory.setUpdateTime(new Date());
        if(categoryMapper.updateByPrimaryKeySelective(goodsCategory) > 0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        return categoryMapper.deleteBatch(ids) > 0;
    }
}
