package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.AdminUserToken;

public interface AdminUserTokenMapper {

    AdminUserToken selectByPrimaryKey(Long adminUserId);

    int insertSelective(AdminUserToken adminUserToken);

    int updateByPrimaryKeySelective(AdminUserToken adminUserToken);

    AdminUserToken selectByToken(String token);

    int deleteByPrimaryKey(Long adminUserId);
}
