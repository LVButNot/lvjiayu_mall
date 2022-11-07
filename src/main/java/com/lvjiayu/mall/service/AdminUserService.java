package com.lvjiayu.mall.service;

import com.lvjiayu.mall.entity.AdminUser;


public interface AdminUserService {
    /**
     * 管理员登录模块
     */
    String login(String userName, String password);

    /**
     * 管理员信息展示
     */
    AdminUser selectUserDetailById(Long adminUserId);

    boolean updatePassword(Long adminUserId, String newPassword, String originalPassword);

    boolean updateName(Long adminUserId, String nickName, String loginUserName);

    void logout(Long adminUserId);
}
