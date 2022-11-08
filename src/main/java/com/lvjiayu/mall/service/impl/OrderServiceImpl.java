package com.lvjiayu.mall.service.impl;

import com.lvjiayu.mall.dao.OrderMapper;
import com.lvjiayu.mall.entity.MallOrder;
import com.lvjiayu.mall.service.OrderService;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public PageResult getOrdersPage(PageQueryUtil pageUtil) {
        List<MallOrder> list = orderMapper.findMallOrderList(pageUtil);
        int total = orderMapper.getTotalOrders(pageUtil);
        PageResult pageResult = new PageResult(list, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
