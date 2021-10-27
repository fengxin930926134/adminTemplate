package com.fengx.template.controller.sys;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "系统角色控制器")
@RestController
@RequestMapping(value = "/sys/role")
@RequiredArgsConstructor
public class RoleController {
}