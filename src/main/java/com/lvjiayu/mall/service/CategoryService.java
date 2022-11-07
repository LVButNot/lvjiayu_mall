package com.lvjiayu.mall.service;

import com.lvjiayu.mall.entity.GoodsCategory;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;

import java.util.Collections;
import java.util.List;


public interface CategoryService {

    PageResult getCategoriesPage(PageQueryUtil pageUtil);

    GoodsCategory getGoodsCategoryById(Long categoryId);

    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    Boolean deleteBatch(Long[] ids);
}
