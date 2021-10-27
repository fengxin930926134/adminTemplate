package com.fengx.template.service;

import com.fengx.template.pojo.entity.sys.User;

public interface PublicService {

    /**
     * 身份验证
     *
     * @param username 用户名
     * @param password 密码
     * @return User
     */
    User authentication(String username, String password);
}
