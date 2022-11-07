package com.lvjiayu.mall.controller.admin.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class BatchIdParam implements Serializable {
    Long[] ids;

}
