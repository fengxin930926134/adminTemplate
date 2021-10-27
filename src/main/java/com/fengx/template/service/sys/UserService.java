package com.fengx.template.service.sys;

import com.fengx.template.pojo.param.sys.LoginParam;
import com.fengx.template.pojo.param.sys.PwdParam;
import com.fengx.template.response.Response;

public interface UserService {

    /**
     * 登录验证账号密码
     * 认证，加权，缓存
     *
     * @param param LoginParam
     * @return LoginVo
     */
    Response<?> login(LoginParam param);

    /**
     * 修改密码
     *
     * @param pwdParam pwdParam
     * @return Response
     */
    Response<?> upPassword(PwdParam pwdParam);
}
