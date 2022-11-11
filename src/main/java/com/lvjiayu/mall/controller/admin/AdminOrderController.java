package com.lvjiayu.mall.controller.admin;

import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.config.annotation.TokenToAdminUser;
import com.lvjiayu.mall.controller.admin.param.BatchIdParam;
import com.lvjiayu.mall.controller.mall.vo.OrderDetailVo;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.service.OrderService;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.Result;
import com.lvjiayu.mall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.image.RGBImageFilter;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "8-5.后台管理系统订单模块接口")
@RequestMapping("/manage-api/v1")
public class AdminOrderController {
    @Resource
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

    /**
     * 列表
     */
    @GetMapping("/orders")
    @ApiOperation(value = "订单列表", notes = "可根据订单号和订单状态筛选")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "订单号") String orderNo,
                       @RequestParam(required = false) @ApiParam(value = "订单状态") Integer orderStatus,
                       @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10){
            return ResultGenerator.genFailResult("参数异常");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if(StringUtils.hasText(orderNo)){
            params.put("orderNo", orderNo);
        }
        if(orderStatus != null){
            params.put("orderStatus", orderStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(orderService.getOrdersPage(pageUtil));
    }

    /**
     * 详情
     */
    @GetMapping("/orders/{orderId}")
    @ApiOperation(value = "订单详情接口", notes = "传参为订单号")
    public Result<OrderDetailVo> info(@ApiParam(value = "订单号") @PathVariable("orderId") Long orderId, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        return ResultGenerator.genSuccessResult(orderService.getOrderDetailByOrderId(orderId));
    }

    /**
     * 配货
     */
    @PutMapping("/orders/checkDone")
    @ApiOperation(value = "修改订单为配货成功", notes = "批量修改")
    public Result checkDone(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(batchIdParam == null || batchIdParam.getIds().length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        String result = orderService.checkDone(batchIdParam.getIds());
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    /**
     * 出库
     */
    @PutMapping("/orders/checkOut")
    @ApiOperation(value = "修改订单为配货成功", notes = "批量修改")
    public Result checkOut(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(batchIdParam == null || batchIdParam.getIds().length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        String result = orderService.checkOut(batchIdParam.getIds());
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    /**
     * 关闭订单
     */
    @PutMapping("/orders/close")
    @ApiOperation(value = "修改订单为商家关闭", notes = "批量修改")
    public Result closeOrder(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(batchIdParam == null || batchIdParam.getIds().length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        String result = orderService.closeOrder(batchIdParam.getIds());
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }



}
