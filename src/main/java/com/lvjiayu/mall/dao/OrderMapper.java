package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.MallOrder;
import com.lvjiayu.mall.util.PageQueryUtil;

import java.util.List;

public interface OrderMapper {
    List<MallOrder> findMallOrderList(PageQueryUtil pageUtil);

    int getTotalOrders(PageQueryUtil pageUtil);

    MallOrder selectByPrimaryKey(Long orderId);
}
