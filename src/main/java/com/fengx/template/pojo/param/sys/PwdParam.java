package com.fengx.template.pojo.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "修改密码")
public class PwdParam {

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "账号")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "请重复输入密码")
    @ApiModelProperty(value = "第二次输入密码")
    private String rePassword;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
