package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.GoodsCategory;
import com.lvjiayu.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int getTotalGoodsCategories(PageQueryUtil pageUtil);

    List<GoodsCategory> findGoodsCategoryList(PageQueryUtil pageUtil);

    GoodsCategory selectByPrimaryKey(Long categoryId);

    int insertSelective(GoodsCategory goodsCategory);

    GoodsCategory selectByLevelAndName(@Param("categoryLevel") Byte categoryLevel, @Param("categoryName") String categoryName);

    int updateByPrimaryKeySelective(GoodsCategory goodsCategory);

    int deleteBatch(Long[] ids);

    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel, int number);
}
