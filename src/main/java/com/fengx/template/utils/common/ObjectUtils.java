package com.fengx.template.utils.common;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象操作工具
 */
public class ObjectUtils {

    /**
     * 判断是否有值
     * (长度等于0也是无值)
     * @return true 有， false 无
     */
    public static boolean checkValue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String && (((String) obj).length() == 0 || "".equals(obj) || ((String) obj).isEmpty())) {
            return false;
        }
        if (obj instanceof List<?> && CollectionUtils.isEmpty((List<?>) obj)) {
            return false;
        }
        if (obj.getClass().isArray() && new Object[]{ obj }.length == 0) {
            return false;
        }
        if (obj instanceof Map<?, ?> && CollectionUtils.isEmpty(((Map<?, ?>) obj))) {
            return false;
        }
        return (!(obj instanceof Set<?>)) || !CollectionUtils.isEmpty((Set<?>) obj);
    }

    /**
     * 对象属性值的拷贝
     * @param source 源对象
     * @param target 目标对象
     * @return V 目标对象
     */
    public static <T, V> V copy(T source, V target) {
        if (source == null || target == null) {
            return null;
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 根据n平均划分划分List
     * @param list 数据
     * @param n 划分份数
     * @return List<List<T>>
     */
    public static <T> List<List<T>> averageAssign(List<T> list, int n){
        if (n <= 0) {
            return new ArrayList<>();
        }
        List<List<T>> result = new ArrayList<>();
        // 先计算出余数
        int remainder = list.size() % n;
        // 然后是商
        int number = list.size() / n;
        // 偏移量
        int offset = 0;
        for(int i = 0;i < n; i++){
            List<T> value;
            if(remainder > 0){
                value = list.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            }else{
                value = list.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
}
