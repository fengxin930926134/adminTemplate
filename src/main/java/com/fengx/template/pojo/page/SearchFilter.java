package com.fengx.template.pojo.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "构建查询过滤条件参数")
@Data
public class SearchFilter {

    @ApiModelProperty(value = "字段名")
    private String name;

    @ApiModelProperty(value = "数据库过滤类型")
    private Operator type;

    @ApiModelProperty(value = "过滤值")
    private Object value;

    @ApiModelProperty(value = "字段间拼接条件", notes = "and or 默认AND")
    private WhereOperator whereType = WhereOperator.AND;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchFilter that = (SearchFilter) o;
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        if (!Objects.equals(value, that.value)) {
            return false;
        }
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

}
