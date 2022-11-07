package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.MallGoods;

public interface GoodsMapper {

    int insertSelective(MallGoods mallGoods);

    int updateByPrimaryKeySelective(MallGoods mallGoods);

    MallGoods selectByPrimaryKey(Long goodsId);
}
