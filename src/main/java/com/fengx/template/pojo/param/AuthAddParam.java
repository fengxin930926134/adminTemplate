package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
// ApiModel只在参数生效
@ApiModel(value = "添加权限参数", description = "所添加的权限都会给超级管理员角色")
public class AuthAddParam {

    @ApiModelProperty(value = "权限描述")
    @NotBlank(message = "权限描述不能为空")
    private String desc;

    @ApiModelProperty(value = "授权链接", notes = "url通配符为两颗星，比如说 /user下的所有url，应该写成 /user/**")
    @NotBlank(message = "授权链接不能为空")
    private String url;

    @ApiModelProperty(value = "权限类型", notes = "1.不需要登录的权限 2.url需要角色访问权限")
    @NotNull(message = "权限类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "角色ID列表")
    private List<String> roleIds;
}
