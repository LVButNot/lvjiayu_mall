package com.lvjiayu.mall.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryUtil extends LinkedHashMap<String, Object> {

    //当前页码
    private Integer page;
    //每页条数
    private Integer limit;

    public PageQueryUtil(Map<String, Object> params){
        this.putAll(params);
        //分页参数
        page = Integer.parseInt(params.get("page").toString());
        limit = Integer.parseInt(params.get("limit").toString());
        this.put("start", (page-1)*limit);
        this.put("page", page);
        this.put("limit", limit);
    }
}
