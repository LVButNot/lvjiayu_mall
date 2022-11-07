package com.lvjiayu.mall.service.impl;

import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.dao.AdminUserMapper;
import com.lvjiayu.mall.dao.AdminUserTokenMapper;
import com.lvjiayu.mall.entity.AdminUser;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.service.AdminUserService;
import com.lvjiayu.mall.util.NumberUtil;
import com.lvjiayu.mall.util.SystemUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import javax.annotation.Resource;
import java.util.Date;
@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private AdminUserTokenMapper adminUserTokenMapper;
    @Override
    public String login(String userName, String password) {
        AdminUser loginAdminUser = adminUserMapper.login(userName, password);
        if(loginAdminUser != null){
            //登陆后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", loginAdminUser.getAdminUserId());
            AdminUserToken adminUserToken = adminUserTokenMapper.selectByPrimaryKey(loginAdminUser.getAdminUserId());
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);
            if(adminUserToken == null){
                adminUserToken = new AdminUserToken();
                adminUserToken.setAdminUserId(loginAdminUser.getAdminUserId());
                adminUserToken.setToken(token);
                adminUserToken.setUpdateTime(now);
                adminUserToken.setExpireTime(expireTime);
                //写入数据库
                if(adminUserTokenMapper.insertSelective(adminUserToken) > 0){
                    return token;
                }
            }else{
                adminUserToken.setToken(token);
                adminUserToken.setUpdateTime(now);
                adminUserToken.setExpireTime(expireTime);
                //写入数据库
                if(adminUserTokenMapper.updateByPrimaryKeySelective(adminUserToken) > 0){
                    return token;
                }
            }
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public AdminUser selectUserDetailById(Long adminUserId) {
        return adminUserMapper.selectByPrimaryKey(adminUserId);
    }

    @Override
    public boolean updatePassword(Long adminUserId, String newPassword, String originalPassword) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(adminUserId);
        if(adminUser != null){
            if(adminUser.getLoginPassword().equals(originalPassword)){
                adminUser.setLoginPassword(newPassword);
                if(adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0
                        && adminUserTokenMapper.deleteByPrimaryKey(adminUserId) > 0){
                    return true;
                }
            }
        }
        return false;


    }

    @Override
    public boolean updateName(Long adminUserId, String nickName, String loginUserName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(adminUserId);
        if(adminUser != null){
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if(adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public void logout(Long adminUserId) {
        adminUserTokenMapper.deleteByPrimaryKey(adminUserId);
    }

    /**
     * 获取token的值
     * @param s 当前时间
     * @param adminUserId 用户的id
     * @return token
     */
    private String getNewToken(String s, Long adminUserId) {
        String src = s + adminUserId + NumberUtil.genRandomNum(6);
        return SystemUtil.genToken(src);
    }
}
