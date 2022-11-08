package com.lvjiayu.mall.controller.admin;

import com.lvjiayu.mall.common.Constants;
import com.lvjiayu.mall.common.IndexConfigTypeEnum;
import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.config.annotation.TokenToAdminUser;
import com.lvjiayu.mall.controller.admin.param.BatchIdParam;
import com.lvjiayu.mall.controller.admin.param.IndexConfigAddParam;
import com.lvjiayu.mall.controller.admin.param.IndexConfigEditParam;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.entity.IndexConfig;
import com.lvjiayu.mall.service.IndexConfigService;
import com.lvjiayu.mall.util.BeanUtil;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.Result;
import com.lvjiayu.mall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "8—4.后台管理系统首页配置模块接口")
@RequestMapping("/manage-api/v1")
public class AdminIndexConfigController {
    @Resource
    private IndexConfigService indexConfigService;

    private static final Logger logger = LoggerFactory.getLogger(AdminIndexConfigController.class);

    @GetMapping("/indexConfigs")
    @ApiOperation(value = "首页配置列表", notes = "首页配置列表")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "1-搜索框热搜 2-搜索下拉框热搜 3-热销商品 4-新品上线 5-为你推荐") Integer configType,
                       @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10){
            return ResultGenerator.genFailResult("参数异常");
        }
        IndexConfigTypeEnum indexConfigTypeEnum = IndexConfigTypeEnum.getIndexConfigTypeEnumByType(configType);
        if(indexConfigTypeEnum == IndexConfigTypeEnum.DEFAULT){
            return ResultGenerator.genFailResult("参数异常");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("configType", configType);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(indexConfigService.getConfigsPage(pageUtil));
    }

    @PostMapping("/indexConfigs")
    @ApiOperation(value = "新增首页配置项", notes = "新增首页配置项")
    public Result save(@RequestBody @Valid IndexConfigAddParam indexConfigAddParam,
                       @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        IndexConfig indexConfig = new IndexConfig();
        BeanUtil.copyProperties(indexConfigAddParam,indexConfig);
        String result = indexConfigService.saveIndexConfig(indexConfig);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @PutMapping("/indexConfigs")
    @ApiOperation(value = "修改首页配置项", notes = "修改首页配置项")
    public Result update(@RequestBody @Valid IndexConfigEditParam indexConfigEditParam,
                         @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        IndexConfig indexConfig = new IndexConfig();
        BeanUtil.copyProperties(indexConfigEditParam,indexConfig);
        String result = indexConfigService.updateIndexConfig(indexConfig);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @GetMapping("/indexConfigs/{id}")
    @ApiOperation(value = "获取单条首页配置项信息", notes = "根据id查询")
    public Result info(@PathVariable("id") Long configId, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        IndexConfig indexConfig = indexConfigService.getIndexConfigById(configId);
        if(ObjectUtils.isEmpty(indexConfig)){
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(indexConfig);
    }

    @DeleteMapping("/indexConfigs")
    @ApiOperation(value = "批量删除首页配置项信息", notes = "批量删除首页配置项信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam,
                         @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if (ObjectUtils.isEmpty(batchIdParam) || batchIdParam.getIds().length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(indexConfigService.deleteBatch(batchIdParam.getIds())){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("批量删除失败");
    }
}
