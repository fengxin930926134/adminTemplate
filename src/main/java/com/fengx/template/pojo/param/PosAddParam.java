package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
// ApiModel只在参数生效
@ApiModel(value = "添加位置参数")
public class PosAddParam {

    @NotBlank(message = "位置名称不能为空")
    @ApiModelProperty(value = "位置名称")
    private String posName;

    @NotNull(message = "经度不能为空")
    @ApiModelProperty(value = "经度")
    private double latitude;

    @NotNull(message = "纬度不能为空")
    @ApiModelProperty(value = "纬度")
    private double altitude;
}
