package com.fengx.template.pojo.param.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
// ApiModel只在参数生效
@ApiModel(value = "标识参数")
public class IdParam {

    @ApiModelProperty(value = "唯一标识ID")
    @NotBlank(message = "ID不能为空")
    private String id;
}
