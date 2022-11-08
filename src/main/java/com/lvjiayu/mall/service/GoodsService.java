package com.lvjiayu.mall.service;

import com.lvjiayu.mall.entity.MallGoods;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;


public interface GoodsService {

    String insertMallGoods(MallGoods mallGoods);

    String updateMallGoods(MallGoods mallGoods);

    PageResult getGoodsPage(PageQueryUtil pageUtil);
}
