package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
// ApiModel只在参数生效
@ApiModel(value = "添加费用参数")
public class CostAddParam {

    @ApiModelProperty(value = "费用名称")
    @NotBlank(message = "费用名称不能为空")
    private String costName;

    @ApiModelProperty(value = "费用")
    @NotNull(message = "费用不能为空")
    private double cost;
}
