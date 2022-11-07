package com.lvjiayu.mall.controller.admin;

import com.lvjiayu.mall.common.CategoryLevelEnum;
import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.config.annotation.TokenToAdminUser;
import com.lvjiayu.mall.controller.admin.param.BatchIdParam;
import com.lvjiayu.mall.controller.admin.param.GoodsCategoryAddParam;
import com.lvjiayu.mall.controller.admin.param.GoodsCategoryEditParam;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.entity.GoodsCategory;
import com.lvjiayu.mall.service.CategoryService;
import com.lvjiayu.mall.util.BeanUtil;
import com.lvjiayu.mall.util.PageQueryUtil;
import com.lvjiayu.mall.util.Result;
import com.lvjiayu.mall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "8-2.后台管理系统分类模块接口")
@RequestMapping("/manager-api/v1")
public class AdminGoodsCategoryController {
    private static final Logger logger = LoggerFactory.getLogger(AdminGoodsCategoryController.class);
    @Resource
    private CategoryService categoryService;

    /**
     * 列表接口一
     */
    @GetMapping("/categories")
    @ApiOperation(value = "商品分类列表", notes = "根据级别和上级分类id查询")
    public Result list(@RequestParam @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam @ApiParam(value = "每页条数") Integer pageSize,
                       @RequestParam @ApiParam(value = "分类级别") Integer categoryLevel,
                       @RequestParam @ApiParam(value = "上级分类的id") Integer parentId,
                       @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("AdminUser:{}", adminUserToken.toString());
        if(pageNumber == null || pageNumber < 1 ||
           pageSize == null || pageSize < 10 ||
           categoryLevel == null || categoryLevel > 3 || categoryLevel < 0 ||
           parentId == null || parentId < 0){
            return ResultGenerator.genFailResult("参数异常");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("categoryLevel", categoryLevel);
        params.put("parentId",parentId);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(categoryService.getCategoriesPage(pageUtil));
    }

    /**
     * 列表接口二
     */
    @GetMapping("/categories4Select")
    @ApiOperation(value = "商品分类列表", notes = "用于制作三级分类联动效果")
    public Result listForSelect(@RequestParam("categoryId") Long categoryId, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("AdminUser:{}", adminUserToken.toString());
        if(categoryId == null || categoryId < 1){
            return ResultGenerator.genFailResult("缺少参数！");
        }
        GoodsCategory category = categoryService.getGoodsCategoryById(categoryId);
        //既不为一级分类也不为二级分类则为不返回数据
        if(category == null || category.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()){
            return ResultGenerator.genFailResult("参数异常");
        }
        Map categoryResult = new HashMap(4);
        if(category.getCategoryLevel() == CategoryLevelEnum.LEVEL_ONE.getLevel()){
            //此时查询的是某个一级分类下的二级分类，同时还有第一个二级分类下的所有三级分类
            //查询一级分类列表中第一个实体下所有的二级分类
            List<GoodsCategory> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), CategoryLevelEnum.LEVEL_TWO.getLevel());
            if(!CollectionUtils.isEmpty(secondLevelCategories)){
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if(category.getCategoryLevel() == CategoryLevelEnum.LEVEL_TWO.getLevel()){
            //如果是二级分类，返回当前分类下的所有三级分类列表
            List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), CategoryLevelEnum.LEVEL_THREE.getLevel());
            categoryResult.put("thirdLevelCategories", thirdLevelCategories);
        }
        return ResultGenerator.genSuccessResult(categoryResult);

    }

    /**
     * 添加
     */
    @PostMapping("/categories")
    @ApiOperation(value = "新增分类", notes = "新增分类")
    public Result save(@RequestBody @Valid GoodsCategoryAddParam goodsCategoryAddParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtil.copyProperties(goodsCategoryAddParam, goodsCategory);
        String result = categoryService.saveCategory(goodsCategory);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 更改
     */
    @PutMapping("/categories")
    @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody @Valid GoodsCategoryEditParam goodsCategoryEditParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtil.copyProperties(goodsCategoryEditParam, goodsCategory);
        String result = categoryService.updateGoodsCategory(goodsCategory);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 详情
     */
    @GetMapping("/categories/{categoryId}")
    @ApiOperation(value = "获取单条分类信息", notes = "根据id查询")
    public Result info(@PathVariable("categoryId") Long categoryId, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        GoodsCategory goodsCategory = categoryService.getGoodsCategoryById(categoryId);
        if(goodsCategory == null){
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(goodsCategory);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/categories")
    @ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(batchIdParam == null || batchIdParam.getIds().length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if(categoryService.deleteBatch(batchIdParam.getIds())){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}
