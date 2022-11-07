package com.lvjiayu.mall.controller.admin.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class CarouselAddParam {
    @ApiModelProperty("轮播图URL地址")
    @NotEmpty(message = "轮播图URL地址不能为空")
    private String carouselUrl;

    @ApiModelProperty("轮播图URL跳转地址")
    @NotEmpty(message = "轮播图URL跳转地址不能为空")
    private String redirectUrl;

    @ApiModelProperty("排序值")
    @Max(value = 200, message = "排序值最高为200")
    @Min(value = 1, message = "排序值最低为1")
    @NotEmpty(message = "排序值不能为空")
    private String carouselRank;
}
