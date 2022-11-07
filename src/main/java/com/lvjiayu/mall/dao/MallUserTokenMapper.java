package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.MallUserToken;

public interface MallUserTokenMapper {
    MallUserToken selectByToken(String token);

}
