package com.fengx.template.pojo.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "排序参数")
public class SortMeta {

    @ApiModelProperty(value = "排序字段")
    private String field;

    @ApiModelProperty(value = "排序规则", notes = "1 -> 升序asc，-1 -> 降序desc")
    private int order;
}
