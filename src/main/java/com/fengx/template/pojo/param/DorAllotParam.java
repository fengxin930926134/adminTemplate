package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
// ApiModel只在参数生效
@ApiModel(value = "宿舍分配参数")
public class DorAllotParam {

    @ApiModelProperty(value = "宿舍ID")
    @NotBlank(message = "未选择宿舍")
    private String dorId;

    @ApiModelProperty(value = "用户ID")
    private List<String> userIds;
}
