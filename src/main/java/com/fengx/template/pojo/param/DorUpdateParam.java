package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
// ApiModel只在参数生效
@ApiModel(value = "修改宿舍参数")
public class DorUpdateParam {

    @ApiModelProperty(value = "唯一标识ID")
    @NotBlank(message = "ID不能为空")
    private String id;

    @ApiModelProperty(value = "宿舍编号", notes = "如: A3-410")
    @NotBlank(message = "宿舍编号不能为空")
    private String dno;

    @ApiModelProperty(value = "楼层", notes = "如: 4")
    @NotNull(message = "楼层不能为空")
    private int floor;

    @ApiModelProperty(value = "寝室类型", notes = "0.女寝 1.男寝")
    @NotNull(message = "寝室类型不能为空")
    private int type;

    @ApiModelProperty(value = "位置表id")
    @NotNull(message = "坐标位置不能为空")
    private String posId;

    @ApiModelProperty(value = "可容纳人数")
    @NotNull(message = "可容纳人数不能为空")
    private int holdNum;
}
