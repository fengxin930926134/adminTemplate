package com.fengx.template.pojo.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "登录参数")
public class LoginParam {

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "账号")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String code;

    @NotBlank(message = "验证码ID不能为空")
    @ApiModelProperty(value = "验证码ID", notes = "由前端uuid生成")
    private String codeId;
}
