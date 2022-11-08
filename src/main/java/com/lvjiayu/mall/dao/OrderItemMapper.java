package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.MallOrderItem;

import java.util.List;

public interface OrderItemMapper {
    List<MallOrderItem> selectByOrderId(Long orderId);
}
