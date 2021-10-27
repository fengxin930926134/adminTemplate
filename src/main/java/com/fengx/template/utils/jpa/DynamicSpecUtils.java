package com.fengx.template.utils.jpa;

import com.fengx.template.pojo.page.Pager;
import com.fengx.template.pojo.page.WhereOperator;
import com.fengx.template.pojo.page.SearchFilter;
import com.fengx.template.utils.common.ObjectUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 动态构建jpa查询语言工具
 */
@Slf4j
public class DynamicSpecUtils {
    /**
     * 年月日时间格式
     */
    private static final String SHORT_DATE = "yyyy-MM-dd";
    /**
     * 年月日时分秒时间格式
     */
    private static final String LONG_DATE = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时分秒时间格式
     */
    private static final String TIME = "HH:mm:ss";

    private static final String PERCENTAGE_NUMBER = "%";

    private static final String SPOT = ".";

    private static final String COMMA = ",";


    /**
     * 构建动态查询语句
     *
     * root 表示要构建的实体类信息
     * query 相当于查询数据库中关键字，是一个接口
     * builder 构建查询信息
     * @param filterSet 过滤条件
     * @return org.springframework.data.jpa.domain.Specification<T>
     */
    public static <T> Specification<T> bySearchFilter(final Set<SearchFilter> filterSet) {
        return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            if (ObjectUtils.checkValue(filterSet)) {
                List<Predicate> predicates = Lists.newArrayList();
                List<Predicate> andPredicates = Lists.newArrayList();
                // 构建查询对象
                filterSet.forEach(filter -> {
                    // 实体类字段名称
                    String[] names = StringUtils.split(filter.getName(), SPOT);
                    // 得到构造查询,字段名称
                    Path expression = root.get(names[0]);
                    for (int i = 1; i < names.length; i++) {
                        expression = expression.get(names[i]);
                    }
                    // 处理时间，或者枚举
                    Class clazz = dealDateAndEnum(filter, expression);
                    if (filter.getWhereType().equals(WhereOperator.OR)) {
                        setBuilder(criteriaBuilder, predicates, filter, expression, clazz);
                    } else {
                        setBuilder(criteriaBuilder, andPredicates, filter, expression, clazz);
                    }
                });
                if (CollectionUtils.isNotEmpty(predicates) && CollectionUtils.isNotEmpty(andPredicates)) {
                    Predicate or = criteriaBuilder.or(predicates.toArray(new Predicate[0]));
                    Predicate and = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
                    return criteriaBuilder.and(or, and);
                } else if (CollectionUtils.isNotEmpty(predicates) && CollectionUtils.isEmpty(andPredicates)) {
                    return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
                } else {
                    return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
                }
            }
            return criteriaBuilder.conjunction();
        };
    }

    /**
     * 处理日期和枚举
     * @param filter     过滤值
     * @param expression 表达式
     * @return java.lang.Class
     */
    private static Class dealDateAndEnum(SearchFilter filter, Path expression) {
        // 进行日期，枚举等转换
        Class clazz = expression.getJavaType();
        if (Date.class.isAssignableFrom(clazz) &&
                !filter.getValue().getClass().equals(clazz)) {
            String value = (String) filter.getValue();
            if (!value.contains(COMMA)) {
                filter.setValue(transform2Date(value));
            }
        } else if (LocalDateTime.class.isAssignableFrom(clazz) &&
                !filter.getValue().getClass().equals(clazz)
        ) {
            // 时间很有可能包含between
            String value = (String) filter.getValue();
            if (!value.contains(COMMA)) {
                filter.setValue(transform2LocalDateTime(value));
            }
        } else if (LocalDate.class.isAssignableFrom(clazz) &&
                !filter.getValue().getClass().equals(clazz)
        ) {
            String value = (String) filter.getValue();
            if (!value.contains(COMMA)) {
                filter.setValue(transform2LocalDate(value));
            }
        }
        if (Enum.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
            filter.setValue(transform2Enum(clazz, (String) filter.getValue()));
        }
        return clazz;
    }

    /**
     * 配置查询语句
     * @param criteriaBuilder 构建查询语句
     * @param predicates,     查询构建对象数组
     * @param filter          过滤值
     * @param expression      表达式
     * @param clazz           实体类
     */
    private static void setBuilder(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, SearchFilter filter, Path expression, Class clazz) {
        // 设置匹配模式
        Predicate predicate = null;
        switch (filter.getType()) {
            case EQ:
                predicate = criteriaBuilder.equal(expression, filter.getValue());
                break;
            case LIKE:
                predicate = criteriaBuilder.like(expression, PERCENTAGE_NUMBER + filter.getValue() + PERCENTAGE_NUMBER);
                break;
            case LLIKE:
                predicate = criteriaBuilder.like(expression, PERCENTAGE_NUMBER + filter.getValue());
                break;
            case RLIKE:
                predicate = criteriaBuilder.like(expression, filter.getValue() + PERCENTAGE_NUMBER);
                break;
            case GT:
                predicate = criteriaBuilder.greaterThan(expression, (Comparable) filter.getValue());
                break;
            case GTE:
                predicate = criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue());
                break;
            case LT:
                predicate = criteriaBuilder.lessThan(expression, (Comparable) filter.getValue());
                break;
            case LTE:
                predicate = criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) filter.getValue());
                break;
            case IN:
                predicate = expression.in((Object[]) filter.getValue().toString().split(COMMA));
                break;
            case ISNULL:
                predicate = criteriaBuilder.isNull(expression);
                break;
            case ISNOTNULL:
                predicate = criteriaBuilder.isNotNull(expression);
                break;
            case NOTEQ:
                predicate = criteriaBuilder.notEqual(expression, filter.getValue());
                break;
            case BETWEEN:
                // 时间between 样式: 2018-09-05 11:22:333, 2018-09-05 11:26:333
                // 字符串between 样式
                if (LocalDateTime.class.isAssignableFrom(clazz)) {
                    predicate = criteriaBuilder.between(expression, getDate(filter.getValue(), 0), getDate(filter.getValue(), 1));
                } else if (LocalDate.class.isAssignableFrom(clazz)) {
                    predicate = criteriaBuilder.between(expression, transform2LocalDate(((String) filter.getValue()).split(COMMA)[0])
                            , transform2LocalDate(((String) filter.getValue()).split(COMMA)[1]));
                } else if (String.class.isAssignableFrom(clazz)) {
                    if (filter.getValue() != null) {
                        predicate = criteriaBuilder.between(expression, ((String) filter.getValue()).split(COMMA)[0]
                                , ((String) filter.getValue()).split(COMMA)[1]);
                    }
                }
                break;
            case NOTIN:
                predicate = criteriaBuilder.not(expression.in(new Object[]{filter.getValue().toString().split(COMMA)}));
                break;
        }
        if (predicate == null) {
            return;
        }
        predicates.add(predicate);
    }

    /**
     * @param value 日期值
     * @param i     需要截取位数
     * @return java.time.LocalDateTime
     */
    private static LocalDateTime getDate(Object value, int i) {
        String s = value == null ? "" : (String) value;
        String s1 = s.split(COMMA)[i];
        if ("undefined".equals(s1) || "null".equals(s1)) {
            return LocalDateTime.now();
        }
        if (s1.length() == 10) {
            return LocalDateTime.of(LocalDate.parse(s1), LocalTime.MIN);
        }
        return LocalDateTime.parse(s1, DateTimeFormatter.ofPattern(LONG_DATE));
    }

    public static <T> Specification<T> bySearchFilter(Pager pager) {
        return bySearchFilter(pager.getFilters() != null? pager.getFilters(): new HashSet<>());
    }

    public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> searchFilters) {
        return bySearchFilter(searchFilters != null? searchFilters.toArray(new SearchFilter[]{}): new SearchFilter[]{});
    }

    public static <T> Specification<T> bySearchFilter(final SearchFilter... searchFilters) {
        return bySearchFilter(new HashSet<>(Arrays.asList(searchFilters)));
    }

    /**
     * 转为LocalDateTime时间
     * @param value 日期时间字符串
     * @return java.time.LocalDateTime
     */
    private static LocalDateTime transform2LocalDateTime(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(LONG_DATE));
    }

    /**
     * 字符串日期转日期
     * @param value 日期字符串
     * @return java.util.Date
     */
    private static LocalDate transform2LocalDate(String value) {
        return LocalDate.parse(value);
    }

    /**
     * 字符串日期转日期
     * @param dateString 日期字符串
     * @return java.util.Date
     */
    private static Date transform2Date(String dateString) {
        SimpleDateFormat sFormat = new SimpleDateFormat(SHORT_DATE);
        try {
            return sFormat.parse(dateString);
        } catch (ParseException e) {
            try {
                return sFormat.parse(LONG_DATE);
            } catch (ParseException e1) {
                try {
                    return sFormat.parse(TIME);
                } catch (ParseException e2) {
                    log.error("构建jpa查询日志转换错误! 字符串日期:{}", dateString);
                }
            }
        }
        return null;
    }

    /**
     * 将字符串转化为指定枚举类
     * @param enumClass  枚举类
     * @param enumString 枚举字符串
     */
    private static <E extends Enum<E>> E transform2Enum(Class<E> enumClass, String enumString) {
        return EnumUtils.getEnum(enumClass, enumString);
    }
}
