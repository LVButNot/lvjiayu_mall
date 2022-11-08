package com.lvjiayu.mall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
@Data
public class MallOrderItem {
    private Long orderItemId;
    private Long orderId;
    private Long goodsId;
    private String goodsName;
    private String goodsCoverImg;
    private Integer sellingPrice;
    private Integer goodsCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "GMT+8")
    private Date createTime;
}
