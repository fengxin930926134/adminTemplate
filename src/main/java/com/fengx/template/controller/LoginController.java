package com.fengx.template.controller;

import com.fengx.template.exception.PatchaException;
import com.fengx.template.pojo.param.LoginParam;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.PatchcaService;
import com.fengx.template.service.sys.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "系统登录控制器")
@Slf4j
@RestController
@RequestMapping(value = "/sys/authorizing")
@RequiredArgsConstructor
public class LoginController {

    private final @NonNull PatchcaService patchcaService;
    private final @NonNull SysUserService sysUserService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParam(dataTypeClass = LoginParam.class)
    public Response<?> loginValidate(@Valid @RequestBody LoginParam param) {
        // 验证码验证
        patchcaService.validate(param.getCodeId(), param.getCode());
        return sysUserService.login(param);
    }

    @PostMapping(value = "/loginNoCode")
    @ApiOperation(value = "登录无验证码", notes = "发布时关闭访问")
    @ApiImplicitParam(dataTypeClass = LoginParam.class)
    public Response<?> loginNoCode(@Valid @RequestBody LoginParam param) {
        return sysUserService.login(param);
    }

    @GetMapping(value = "/patchca")
    @ApiOperation(value = "获取验证码图片")
    @ApiImplicitParam(dataTypeClass = String.class)
    public void patchca(@NonNull String codeId, HttpServletRequest request, HttpServletResponse response) throws PatchaException {
        patchcaService.doPatcha(codeId, request, response);
    }
}
