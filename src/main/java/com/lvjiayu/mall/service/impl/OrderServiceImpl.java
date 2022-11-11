package com.lvjiayu.mall.service.impl;

import com.lvjiayu.mall.common.NewBeeMallException;
import com.lvjiayu.mall.common.OrderStatusEnum;
import com.lvjiayu.mall.common.PayTypeEnum;
import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.controller.mall.vo.OrderDetailVo;
import com.lvjiayu.mall.controller.mall.vo.OrderItemVO;
import com.lvjiayu.mall.dao.OrderItemMapper;
import com.lvjiayu.mall.dao.OrderMapper;
import com.lvjiayu.mall.entity.MallOrder;
import com.lvjiayu.mall.entity.MallOrderItem;
import com.lvjiayu.mall.service.OrderService;
import com.lvjiayu.mall.util.BeanUtil;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;

    @Override
    public PageResult getOrdersPage(PageQueryUtil pageUtil) {
        List<MallOrder> list = orderMapper.findMallOrderList(pageUtil);
        int total = orderMapper.getTotalOrders(pageUtil);
        return new PageResult(list, total, pageUtil.getLimit(), pageUtil.getPage());
    }

    @Override
    public OrderDetailVo getOrderDetailByOrderId(Long orderId) {
        MallOrder mallOrder = orderMapper.selectByPrimaryKey(orderId);
        if(mallOrder == null){
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        List<MallOrderItem> orderItems = orderItemMapper.selectByOrderId(mallOrder.getOrderId());
        //获取订单项数据
        if(!CollectionUtils.isEmpty(orderItems)){
            List<OrderItemVO> orderItemVOS = BeanUtil.copyList(orderItems, OrderItemVO.class);
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            BeanUtil.copyProperties(mallOrder, orderDetailVo);
            orderDetailVo.setOrderStatusString(OrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(orderDetailVo.getOrderStatus()).getName());
            orderDetailVo.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(orderDetailVo.getPayType()).getName());
            orderDetailVo.setOrderItemVOS(orderItemVOS);
            return orderDetailVo;
        }else{
            NewBeeMallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        List<MallOrder> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if(!CollectionUtils.isEmpty(orders)){
            for(MallOrder order : orders){
                if(order.getIsDeleted() == 1){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                    continue;
                }
                if(order.getOrderStatus() != 1){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                }
            }
            if(!StringUtils.hasText(errorOrderNos.toString())){
            //订单状态正常，可以执行配货完成操作，修改订单状态和更新时间
                if(orderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                }else{
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            }else{
                if(errorOrderNos.length() > 0 && errorOrderNos.length() < 100){
                    return errorOrderNos + "订单的状态不是支付成功无法执行配货操作";
                }else{
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //先将所有的订单全部搜索出来
        List<MallOrder> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if(!CollectionUtils.isEmpty(orders)){
            for(MallOrder order : orders){
                if(order.getIsDeleted() == 1){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                    continue;
                }
                if(order.getOrderStatus() != 1 && order.getOrderStatus() != 2){
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                }
            }
            if(!StringUtils.hasText(errorOrderNos.toString())){
                //订单状态正常，可以执行配货完成操作，修改订单状态和更新时间
                if(orderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                }else{
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            }else{
                if(errorOrderNos.length() > 0 && errorOrderNos.length() < 100){
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                }else{
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //先将所有的订单全部搜索出来
        List<MallOrder> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        StringBuilder errorOrderNos = new StringBuilder();
        if(!CollectionUtils.isEmpty(orders)) {
            for (MallOrder order : orders) {
                if (order.getIsDeleted() == 1) {
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                    continue;
                }
                if (order.getOrderStatus() == 4 || order.getOrderStatus() < 0) {
                    errorOrderNos.append(order.getOrderNo()).append(" ");
                }
            }
            if (!StringUtils.hasText(errorOrderNos.toString())) {
                //订单状态正常，可以执行配货完成操作，修改订单状态和更新时间
                if (orderMapper.closeOrder(Arrays.asList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单无法执行关闭操作";
                }
            }
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
}