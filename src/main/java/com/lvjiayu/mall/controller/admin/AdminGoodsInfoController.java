package com.lvjiayu.mall.controller.admin;

import com.lvjiayu.mall.common.Constants;
import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.config.annotation.TokenToAdminUser;
import com.lvjiayu.mall.controller.admin.param.BatchIdParam;
import com.lvjiayu.mall.controller.admin.param.GoodsAddParam;
import com.lvjiayu.mall.controller.admin.param.GoodsEditParam;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.entity.MallGoods;
import com.lvjiayu.mall.service.GoodsService;
import com.lvjiayu.mall.util.BeanUtil;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.Result;
import com.lvjiayu.mall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api(value = "v1", tags = "8-3.后台管理系统商品模块接口")
@RequestMapping("/manage-api/v1")
public class AdminGoodsInfoController {
    @Resource
    private GoodsService goodsService;

    public static final Logger logger = LoggerFactory.getLogger(AdminGoodsInfoController.class);

    /**
     * 新增商品
     */
    @PostMapping("/goods")
    @ApiOperation(value = "新增商品信息", notes = "新增商品信息")
    public Result save(@RequestBody @Valid GoodsAddParam goodsAddParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        MallGoods mallGoods = new MallGoods();
        BeanUtil.copyProperties(goodsAddParam, mallGoods);
        String result = goodsService.insertMallGoods(mallGoods);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改商品信息
     */
    @PutMapping("/goods")
    @ApiOperation(value = "修改商品信息", notes = "修改商品信息")
    public Result udpate(@RequestBody @Valid GoodsEditParam goodsEditParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        MallGoods mallGoods = new MallGoods();
        BeanUtil.copyProperties(goodsEditParam, mallGoods);
        String result = goodsService.updateMallGoods(mallGoods);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 商品列表（分页）
     */
    @GetMapping("/goods/list")
    @ApiOperation(value = "商品列表", notes = "可根据名称和上架状态筛选")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每条页数") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "商品名称") String goodsName,
                       @RequestParam(required = false) @ApiParam(value = "上架状态 0-上架 1-下架") Integer goodsSellStatus,
                       @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10){
            return ResultGenerator.genFailResult("参数异常");
        }
        Map params = new HashMap(8);
        params.put("page",pageNumber);
        params.put("limit", pageSize);
        if(StringUtils.hasText(goodsName)){
            params.put("goodsName", goodsName);
        }
        if(goodsSellStatus != null){
            params.put("goodsSellStatus", goodsSellStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(goodsService.getGoodsPage(pageUtil));
    }

    /**
     * 批量上下架
     */
    @PutMapping("/goods/status/{status}")
    @ApiOperation(value = "批量修改销售状态", notes = "批量修改销售状态")
    public Result delete(@RequestBody BatchIdParam batchIdParam, @PathVariable("status") int status, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(ObjectUtils.isEmpty(batchIdParam) || batchIdParam.getIds().length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(status != Constants.SELL_STATUS_DOWN && status != Constants.SELL_STATUS_UP){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(goodsService.batchUpdateSellStatus(batchIdParam.getIds(), status)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("修改失败");
    }
}
