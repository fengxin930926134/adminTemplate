package com.fengx.template.controller.sys;

import com.fengx.template.pojo.param.sys.PwdParam;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Api(tags = "系统用户控制器")
@RestController
@RequestMapping(value = "/sys/user")
@RequiredArgsConstructor
public class UserController {

    private final @NonNull UserService userService;

    @PostMapping("/upPassword")
    @ApiOperation(value = "修改密码")
    @ApiImplicitParam(dataTypeClass = PwdParam.class)
    public Response<?> upPassword(@Valid @RequestBody PwdParam pwdParam) {
        return userService.upPassword(pwdParam);
    }
}
