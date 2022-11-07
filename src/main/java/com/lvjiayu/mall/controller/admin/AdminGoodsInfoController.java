package com.lvjiayu.mall.controller.admin;

import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.config.annotation.TokenToAdminUser;
import com.lvjiayu.mall.controller.admin.param.GoodsAddParam;
import com.lvjiayu.mall.controller.admin.param.GoodsEditParam;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.entity.MallGoods;
import com.lvjiayu.mall.service.GoodsService;
import com.lvjiayu.mall.util.BeanUtil;
import com.lvjiayu.mall.util.Result;
import com.lvjiayu.mall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


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
}
