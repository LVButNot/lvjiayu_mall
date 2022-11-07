package com.lvjiayu.mall.controller.admin;




import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.config.annotation.TokenToAdminUser;
import com.lvjiayu.mall.controller.admin.param.BatchIdParam;
import com.lvjiayu.mall.controller.admin.param.CarouselAddParam;
import com.lvjiayu.mall.controller.admin.param.CarouselEditParam;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.entity.Carousel;
import com.lvjiayu.mall.service.CarouselService;
import com.lvjiayu.mall.util.BeanUtil;
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
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/manage-api/v1")
@Api(value = "v1", tags = "8-1.后台管理系统轮播图模块接口")
public class AdminCarouselController {
    private static final Logger logger = LoggerFactory.getLogger(AdminCarouselController.class);

    @Resource
    private CarouselService carouselService;

    /**
     * 列表
     */
    @GetMapping("/carousels")
    @ApiOperation(value = "轮播图列表", notes = "轮播图列表")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                       @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10){
            return ResultGenerator.genFailResult("参数异常");
        }
        Map params = new HashMap(4);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(carouselService.getCarouselPage(pageUtil));
    }
    /**
     * 添加
     */
    @PostMapping("/carousels")
    @ApiOperation(value = "新增轮播图", notes = "新增轮播图")
    public Result save(@RequestBody @Valid CarouselAddParam carouselAddParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if (!StringUtils.hasText(carouselAddParam.getCarouselUrl()) || Objects.isNull(carouselAddParam.getCarouselRank())) {
            return ResultGenerator.genFailResult("参数异常");
        }
        Carousel carousel = new Carousel();
        BeanUtil.copyProperties(carouselAddParam, carousel);
        String result = carouselService.saveCarousel(carousel);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     */
    @PutMapping("/carousels")
    @ApiOperation(value = "修改轮播图信息", notes = "修改轮播图信息")
    public Result update(@RequestBody @Valid CarouselEditParam carouselEditParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        if(!StringUtils.hasText(carouselEditParam.getCarouselUrl())
                || Objects.isNull(carouselEditParam.getCarouselRank())
                || Objects.isNull(carouselEditParam.getCarouselId())){
            return ResultGenerator.genFailResult("参数异常");
        }
        Carousel carousel = new Carousel();
        BeanUtil.copyProperties(carouselEditParam, carousel);
        String result = carouselService.updateCarousel(carousel);
        if(ServiceResultEnum.SUCCESS.getResult().equals(result)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 详情
     */
    @GetMapping("/carousel/{id}")
    @ApiOperation(value = "获取单条轮播图信息", notes = "根据id查询")
    public Result info(@PathVariable Integer id, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        Carousel carousel = carouselService.getCarouselById(id);
        if(carousel == null){
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }else{
            return ResultGenerator.genSuccessResult(carousel);
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/carousel")
    @ApiOperation(value = "批量删除轮播图信息", notes = "批量删除轮播图信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}",adminUserToken.toString());
        Long[] ids = batchIdParam.getIds();
        if(carouselService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除失败");
        }
    }
}
