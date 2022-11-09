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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
        PageResult pageResult = new PageResult(list, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
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
}
