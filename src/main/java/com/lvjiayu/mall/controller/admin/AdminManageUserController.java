package com.lvjiayu.mall.controller.admin;



import com.lvjiayu.mall.common.Constants;
import com.lvjiayu.mall.common.ServiceResultEnum;
import com.lvjiayu.mall.config.annotation.TokenToAdminUser;
import com.lvjiayu.mall.controller.admin.param.AdminLoginParam;
import com.lvjiayu.mall.controller.admin.param.UpdateAdminNameParam;
import com.lvjiayu.mall.controller.admin.param.UpdateAdminPasswordParam;
import com.lvjiayu.mall.entity.AdminUser;
import com.lvjiayu.mall.entity.AdminUserToken;
import com.lvjiayu.mall.service.AdminUserService;
import com.lvjiayu.mall.util.Result;
import com.lvjiayu.mall.util.ResultGenerator;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@RestController
@Api(value = "v1", tags = "后台管理系统管理员模块接口")
@RequestMapping("/manage-api/v1")
public class AdminManageUserController {
    @Resource
    private AdminUserService adminUserService;

    private static final Logger logger = LoggerFactory.getLogger(AdminManageUserController.class);

    /**
     *  登录模块
     */
    @PostMapping("/adminUser/login")
    public Result<String> login(@RequestBody @Valid AdminLoginParam adminLoginParam){
        logger.info("尝试登陆后端管理系统，时间为：{}", new Date().getTime());
        if (adminLoginParam == null
             || !StringUtils.hasText(adminLoginParam.getUserName())
             || !StringUtils.hasText(adminLoginParam.getPasswordMp5())) {
            return ResultGenerator.genFailResult("用户名或密码不能为空");
        }
        String loginResult = adminUserService.login(adminLoginParam.getUserName(), adminLoginParam.getPasswordMp5());
        logger.info("message login api, adminName={}, loginResult={}", adminLoginParam.getUserName(), loginResult);

        //登录成功
        if(StringUtils.hasText(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH){
            Result result = ResultGenerator.genSuccessResult(loginResult);
            return result;
        }
        return ResultGenerator.genFailResult(loginResult);
    }

    /**
     * 管理员信息展示
     */
    @GetMapping("/adminUser/profile")
    public Result profile(@TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        AdminUser adminUserEntity = adminUserService.selectUserDetailById(adminUserToken.getAdminUserId());
        if(adminUserEntity != null){
            adminUserEntity.setLoginPassword("******");
            Result result = ResultGenerator.genSuccessResult(adminUserEntity);
            return result;
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    /**
     * 修改密码模块
     */
    @PutMapping("/adminUser/password")
    public Result passwordUpdate(@TokenToAdminUser AdminUserToken adminUserToken, @RequestBody UpdateAdminPasswordParam adminPasswordParam){
        logger.info("adminUser:{}", adminUserToken.toString());
        if (!StringUtils.hasText(adminPasswordParam.getNewPassword()) || !StringUtils.hasText(adminPasswordParam.getOriginalPassword())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if(adminUserService.updatePassword(adminUserToken.getAdminUserId(),
                                           adminPasswordParam.getNewPassword(),
                                           adminPasswordParam.getOriginalPassword())){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
        }
    }

    /**
     * 修改基本信息模块
     */
    @PutMapping("/adminUser/name")
    public Result nameUpdate(@TokenToAdminUser AdminUserToken adminUserToken, @RequestBody UpdateAdminNameParam adminNameParam){
        logger.info("adminUser:{}", adminUserToken.toString());
        if (!StringUtils.hasText(adminNameParam.getNickName()) || !StringUtils.hasText(adminNameParam.getLoginUserName())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if(adminUserService.updateName(adminUserToken.getAdminUserId(),
                adminNameParam.getNickName(),
                adminNameParam.getLoginUserName())){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult(ServiceResultEnum.DB_ERROR.getResult());
        }
    }

    /**
     * 退出登录接口
     */
    @DeleteMapping("/logout")
    public Result logout(@TokenToAdminUser AdminUserToken adminUserToken){
        logger.info("adminUser:{}", adminUserToken.toString());
        adminUserService.logout(adminUserToken.getAdminUserId());
        return ResultGenerator.genSuccessResult();
    }



}
