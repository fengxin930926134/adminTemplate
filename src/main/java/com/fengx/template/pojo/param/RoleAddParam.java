package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
// ApiModel只在参数生效
@ApiModel(value = "添加权限参数")
public class RoleAddParam {

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色描述")
    @NotBlank(message = "角色描述不能为空")
    private String desc;
}
