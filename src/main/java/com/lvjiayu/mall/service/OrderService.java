package com.lvjiayu.mall.service;

import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;

public interface OrderService {
    PageResult getOrdersPage(PageQueryUtil pageUtil);
}
