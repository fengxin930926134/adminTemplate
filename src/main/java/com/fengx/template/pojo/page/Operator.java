package com.fengx.template.pojo.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "数据库关键字匹配条件枚举")
public enum Operator {

    @ApiModelProperty(value = "等于")
    EQ,

    @ApiModelProperty(value = "模糊查询")
    LIKE,

    @ApiModelProperty(value = "大于")
    GT,

    @ApiModelProperty(value = "小于")
    LT,

    @ApiModelProperty(value = "大于等于")
    GTE,

    @ApiModelProperty(value = "小于等于")
    LTE,

    @ApiModelProperty(value = "在列表中")
    IN,

    @ApiModelProperty(value = "不在列表中")
    NOTIN,

    @ApiModelProperty(value = "左模糊")
    LLIKE,

    @ApiModelProperty(value = "右模糊")
    RLIKE,

    @ApiModelProperty(value = "是空")
    ISNULL,

    @ApiModelProperty(value = "两者之间")
    BETWEEN,

    @ApiModelProperty(value = "不等于")
    NOTEQ,

    @ApiModelProperty(value = "不是空")
    ISNOTNULL
}
