package com.lvjiayu.mall.service;

import com.lvjiayu.mall.controller.mall.vo.OrderDetailVo;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;

public interface OrderService {
    PageResult getOrdersPage(PageQueryUtil pageUtil);

    OrderDetailVo getOrderDetailByOrderId(Long orderId);

    String checkDone(Long[] ids);

    String checkOut(Long[] ids);

    String closeOrder(Long[] ids);
}
