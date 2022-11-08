package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.MallGoods;
import com.lvjiayu.mall.util.PageQueryUtil;

import java.util.List;

public interface GoodsMapper {

    int insertSelective(MallGoods mallGoods);

    int updateByPrimaryKeySelective(MallGoods mallGoods);

    MallGoods selectByPrimaryKey(Long goodsId);

    List<MallGoods> findGoodsList(PageQueryUtil pageUtil);

    int getTotalGoods(PageQueryUtil pageUtil);

    int batchUpdateSellStatus(Long[] ids, Integer status);
}
