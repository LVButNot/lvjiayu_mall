package com.lvjiayu.mall.dao;

import com.lvjiayu.mall.entity.AdminUser;

public interface AdminUserMapper {
    AdminUser login(String userName, String password);

    AdminUser selectByPrimaryKey(Long adminUserId);

    int updateByPrimaryKeySelective(AdminUser adminUser);

}
