package com.fengx.template.service.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PatchcaService {

    /**
     * 获取验证码图片
     * @param appId 随机生成的uuid
     * @param request request
     * @param response response
     */
    void doPatcha(String appId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 检查验证码是否正确
     * @param code code
     */
    void validate(String codeId, String code);
}
