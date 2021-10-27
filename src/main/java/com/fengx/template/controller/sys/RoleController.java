package com.fengx.template.controller.sys;

import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.IdsParam;
import com.fengx.template.pojo.param.RoleAddParam;
import com.fengx.template.pojo.param.RoleUpdateParam;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "系统角色控制器")
@RestController
@RequestMapping(value = "/sys/role")
@RequiredArgsConstructor
public class RoleController {

    private final @NonNull SysRoleService sysRoleService;

    @PostMapping("/page")
    @ApiOperation(value = "分页获取角色信息")
    @ApiImplicitParam(dataTypeClass = PageParam.class)
    public Response<?> page(@NonNull @RequestBody PageParam pageParam) {
        return sysRoleService.page(pageParam);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加角色信息")
    @ApiImplicitParam(dataTypeClass = RoleAddParam.class)
    public Response<?> add(@Valid @RequestBody RoleAddParam roleAddParam) {
        return sysRoleService.add(roleAddParam);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新角色信息")
    @ApiImplicitParam(dataTypeClass = RoleUpdateParam.class)
    public Response<?> update(@Valid @RequestBody RoleUpdateParam roleUpdateParam) {
        return sysRoleService.update(roleUpdateParam);
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除角色信息")
    @ApiImplicitParam(dataTypeClass = IdsParam.class)
    public Response<?> del(@Valid @RequestBody IdsParam idParam) {
        return sysRoleService.delete(idParam);
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取全部角色信息")
    public Response<?> getAll() {
        return sysRoleService.getAll();
    }
}