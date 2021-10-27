package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
// ApiModel只在参数生效
@ApiModel(value = "报到流程修改参数", description = "不包括开始和结束")
public class RegProParam {

    @NotEmpty(message = "流程不能为空")
    @ApiModelProperty(value = "单个流程数组")
    private List<RegProcess> regProList;
}
