package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.MallOrder;
import com.lvjiayu.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    List<MallOrder> findMallOrderList(PageQueryUtil pageUtil);

    int getTotalOrders(PageQueryUtil pageUtil);

    MallOrder selectByPrimaryKey(Long orderId);

    List<MallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkDone(@Param("orderIds") List<Long> asList);

    int checkOut(@Param("orderIds") List<Long> asList);

    int closeOrder(List<Long> asList, int orderStatus);
}
