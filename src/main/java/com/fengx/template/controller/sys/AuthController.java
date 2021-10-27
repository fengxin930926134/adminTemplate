package com.fengx.template.controller.sys;

import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.AuthAddParam;
import com.fengx.template.pojo.param.AuthUpdateParam;
import com.fengx.template.pojo.param.OnOffParam;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.SysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Api(tags = "系统权限控制器")
@RestController
@RequestMapping(value = "/sys/auth")
@RequiredArgsConstructor
public class AuthController {

    private final @NonNull SysPermissionService sysPermissionService;

    @PostMapping("/page")
    @ApiOperation(value = "分页获取url权限信息")
    @ApiImplicitParam(dataTypeClass = PageParam.class)
    public Response<?> page(@NonNull @RequestBody PageParam pageParam) {
        return sysPermissionService.page(pageParam);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加url权限控制")
    @ApiImplicitParam(dataTypeClass = AuthAddParam.class)
    public Response<?> add(@Valid @RequestBody AuthAddParam authAddParam) {
        return sysPermissionService.add(authAddParam);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改url权限控制")
    @ApiImplicitParam(dataTypeClass = AuthUpdateParam.class)
    public Response<?> update(@Valid @RequestBody AuthUpdateParam authUpdateParam) {
        return sysPermissionService.update(authUpdateParam);
    }

    @PostMapping("/onOff")
    @ApiOperation(value = "启用禁用url权限")
    @ApiImplicitParam(dataTypeClass = OnOffParam.class)
    public Response<?> onOff(@Valid @RequestBody OnOffParam onOffParam) {
        return sysPermissionService.onOff(onOffParam);
    }
}
