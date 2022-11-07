package com.lvjiayu.mall.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //业务码，比如成功，失败，权限不足等
    @ApiModelProperty("返回码")
    private int resultCode;
    //返回信息，后端在进行业务处理后给前端返回一个提示信息
    @ApiModelProperty("返回信息")
    private String message;
    //数据结果，任何类型
    @ApiModelProperty("返回数据")
    private T data;
}
