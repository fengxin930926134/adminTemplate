package com.fengx.template.pojo.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回数据
 */
@Data
@NoArgsConstructor
public class PageData {

    /**
     * 数据
     */
    private List<Object> content = new ArrayList<>();

    /**
     * 总记录数
     */
    private long totalElements = 0L;

    /**
     * 总页数
     */
    private int totalPages = 0;

    public <T> PageData(Page<T> page) {
        this.content.addAll(page.getContent());
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public <T> PageData(List<T> content, long totalElements, int totalPages) {
        this.content.addAll(content);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
