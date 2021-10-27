package com.fengx.template.utils.jpa;

import com.fengx.template.pojo.page.Pager;
import com.fengx.template.pojo.page.SortMeta;
import com.google.common.collect.Lists;
import org.springframework.data.domain.*;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 设置起始页
     */
    public static final int START_PAGE = 1;

    /**
     * 设置起始分页长度
     */
    public static final int START_PAGE_SIZE = 10;

    /**
     * 分页方法
     * p: 用作查询条件过于复杂时，无法有效的直接通过数据库查询来分页 （数据量大时会比直接通过数据库查询分页效率低）
     * @param data 全部数据
     * @param startPage 第几页
     * @param pageSize 每页的数据数量
     * @return org.springframework.data.domain.Page
     */
    public static <T> Page<T> page(List<T> data, int startPage, int pageSize) {
        // 分页从起始页开始 需要调整
        int page = changePage(startPage);
        // 构建分页对象
        Pageable pageable = PageRequest.of(page, pageSize);
        // 偏移量大于数据长度
        if (pageable.getOffset() > data.size()) {
            return new PageImpl<>(Lists.newArrayList(), pageable, data.size());
        }

        if (pageable.getOffset() <= data.size() && pageable.getOffset() + pageable.getPageSize() > data.size()) {
            return new PageImpl<>(data.subList((int) pageable.getOffset(), data.size()), pageable, data.size());
        }

        return new PageImpl<>(
                data.subList((int) pageable.getOffset(), (int) pageable.getOffset() + pageable.getPageSize()),
                pageable, data.size());
    }

    /**
     * 通过自定义分页对象构建一个sort排序对象，将所有的排序规则和字段都封装进去
     *
     * @param pager 分页对象
     * @return org.springframework.data.domain.Sort
     */
    public static Sort buildSort(Pager pager) {
        if (pager != null) {
            return buildSort(pager.getSorts());
        }
        return Sort.unsorted();
    }

    /**
     * 通过自定义排序对象构建一个sort排序对象，将所有的排序规则和字段都封装进去
     *
     * @param sortMetas 自定义排序对象
     * @return org.springframework.data.domain.Sort
     */
    public static Sort buildSort(List<SortMeta> sortMetas) {
        return CollectionUtils.isEmpty(sortMetas) ? Sort.unsorted(): Sort.by(buildSortOrder(sortMetas));
    }

    /**
     * 通过分页对象转化为jpa分页排序对象，是对整个数据排序
     *
     * @param pager 分页对象
     * @return org.springframework.data.domain.PageRequest
     */
    public static PageRequest buildPageRequest(Pager pager) {
        // 排序分页
        if (null != pager.getSorts() && pager.getSorts().size() > 0) {
            return buildPageRequest(changePage(pager.getStartPage()), pager.getPageSize(), buildSort(pager));
        }
        // 不排序
        return PageRequest.of(changePage(pager.getStartPage()), pager.getPageSize());
    }

    /**
     * 通过分页对象转化为jpa分页对象
     * 排序为空时 使用自定义排序
     *
     * @param pager 分页对象
     * @param sort  自定义排序对象
     * @return org.springframework.data.domain.PageRequest
     */
    public static PageRequest buildPageRequest(Pager pager, Sort sort) {
        if (!CollectionUtils.isEmpty(pager.getSorts())) {
            return buildPageRequest(pager);
        }
        return PageRequest.of(changePage(pager.getStartPage()), pager.getPageSize(), sort);
    }

    /**
     * 设置分页排序对象
     *
     * @param startRow 开始行
     * @param rows     行数
     * @param sort     排序
     * @return org.springframework.data.domain.PageRequest
     */
    public static PageRequest buildPageRequest(int startRow, int rows, Sort sort) {
        return PageRequest.of(startRow, rows, sort);
    }

    /**
     * 改变页数
     *
     * @param page 页
     * @return int
     */
    private static int changePage(int page) {
        // 页面是从0开始计算 减去起始页数为实际页数
        if (page > 0) {
            page -= START_PAGE;
        }
        return page;
    }

    /**
     * 构建SortOrderList对象
     *
     * @param sortMetas 自定义的排序对象列表
     * @return org.springframework.data.domain.Sort.Order
     */
    private static List<Sort.Order> buildSortOrder(List<SortMeta> sortMetas) {
        if (null != sortMetas && sortMetas.size() > 0) {
            return sortMetas.stream()
                    .map(k -> new Sort.Order(k.getOrder() == -1 ? Sort.Direction.DESC : Sort.Direction.ASC, k.getField()))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
