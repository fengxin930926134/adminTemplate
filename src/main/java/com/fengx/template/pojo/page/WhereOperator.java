package com.fengx.template.pojo.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "数据库关键字匹配条件枚举")
public enum WhereOperator {

    /**
     * and查询
     */
    @ApiModelProperty(value = "and查询")
    AND,

    /**
     * or查询
     */
    @ApiModelProperty(value = "or查询")
    OR;
}
