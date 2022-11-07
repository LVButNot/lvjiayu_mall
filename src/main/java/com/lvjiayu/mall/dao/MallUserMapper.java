package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.MallUser;

public interface MallUserMapper {
    MallUser selectByPrimaryKey(Long userId);

}
