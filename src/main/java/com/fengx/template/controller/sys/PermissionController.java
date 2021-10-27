package com.fengx.template.controller.sys;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统权限控制器")
@RestController
@RequestMapping(value = "/sys/permission")
@RequiredArgsConstructor
public class PermissionController {
}
