package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
// ApiModel只在参数生效
@ApiModel(value = "启用或禁用参数")
public class OnOffParam {

    @ApiModelProperty(value = "唯一标识ID")
    @NotBlank(message = "ID不能为空")
    private String id;

    @ApiModelProperty(value = "开启或者禁用", notes = "1.开启 0.禁用")
    @NotNull(message = "禁用启用参数不能为空")
    private Integer enable;
}
