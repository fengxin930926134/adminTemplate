package com.fengx.template.pojo.page;

import com.fengx.template.utils.jpa.PageUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(value = "分页参数")
public class PageParam {

    @ApiModelProperty(value = "访问的页")
    private int startPage = PageUtils.START_PAGE;

    @ApiModelProperty(value = "一页的数据数量")
    private int pageSize = PageUtils.START_PAGE_SIZE;

    @ApiModelProperty(value = "多项排序条件")
    private List<SortMeta> sorts;

    @ApiModelProperty(value = "多项查询条件")
    private List<SearchFilter> filters;
}
