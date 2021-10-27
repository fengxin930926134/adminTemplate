package com.fengx.template.controller.sys;

import io.swagger.annotations.Api;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "系统角色控制器")
@RestController
@RequestMapping(value = "/sys/role")
@RequiredArgsConstructor
public class RoleController {

//    @PostMapping("/page")
//    @ApiOperation(value = "分页获取角色信息")
//    @ApiImplicitParam(dataTypeClass = Pager.class)
//    public Response<?> page(@NonNull @RequestBody Pager pager) {
//        return sysRoleService.page(pager);
//    }
//
//    @PostMapping("/add")
//    @ApiOperation(value = "添加角色信息")
//    @ApiImplicitParam(dataTypeClass = RoleAddParam.class)
//    public Response<?> add(@Valid @RequestBody RoleAddParam roleAddParam) {
//        return sysRoleService.add(roleAddParam);
//    }
//
//    @PostMapping("/update")
//    @ApiOperation(value = "更新角色信息")
//    @ApiImplicitParam(dataTypeClass = RoleUpdateParam.class)
//    public Response<?> update(@Valid @RequestBody RoleUpdateParam roleUpdateParam) {
//        return sysRoleService.update(roleUpdateParam);
//    }
//
//    @PostMapping("/del")
//    @ApiOperation(value = "删除角色信息")
//    @ApiImplicitParam(dataTypeClass = IdsParam.class)
//    public Response<?> del(@Valid @RequestBody IdsParam idParam) {
//        return sysRoleService.delete(idParam);
//    }
//
//    @GetMapping("/all")
//    @ApiOperation(value = "获取全部角色信息")
//    public Response<?> getAll() {
//        return sysRoleService.getAll();
//    }
}