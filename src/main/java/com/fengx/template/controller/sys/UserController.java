package com.fengx.template.controller.sys;

import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.IdsParam;
import com.fengx.template.pojo.param.PwdParam;
import com.fengx.template.pojo.param.UserAddParam;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;

@Api(tags = "系统用户控制器")
@RestController
@RequestMapping(value = "/sys/user")
@RequiredArgsConstructor
public class UserController {

    private final @NonNull SysUserService sysUserService;

    @PostMapping("/page")
    @ApiOperation(value = "分页获取用户信息")
    @ApiImplicitParam(dataTypeClass = PageParam.class)
    public Response<?> page(@NonNull @RequestBody PageParam pageParam) {
        return sysUserService.page(pageParam);
    }

    @PostMapping("/upPassword")
    @ApiOperation(value = "修改密码")
    @ApiImplicitParam(dataTypeClass = PwdParam.class)
    public Response<?> upPassword(@Valid @RequestBody PwdParam pwdParam) {
        return sysUserService.upPassword(pwdParam);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加用户")
    @ApiImplicitParam(dataTypeClass = UserAddParam.class)
    public Response<?> upPassword(@Valid @RequestBody UserAddParam param) {
        return sysUserService.add(param);
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParam(dataTypeClass = IdsParam.class)
    public Response<?> del(@Valid @RequestBody IdsParam idsParam) {
        return sysUserService.delete(idsParam);
    }

    @PostMapping("/import")
    @ApiOperation(value = "Excel导入学生")
    @ApiImplicitParam(dataTypeClass = MultipartFile.class)
    public Response<?> importExcel(@NonNull MultipartFile file) {
        return sysUserService.importExcel(file);
    }

    @GetMapping("/download")
    @ApiOperation(value = "下载学生信息")
    public Response<?> downloadExcel() {
        return sysUserService.downloadExcel();
    }
}
