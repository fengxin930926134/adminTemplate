package com.fengx.template.utils.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.List;

/**
 * json操作工具类
 */
public class JSONUtils {

    /**
     * java对象转换为JSON字符串
     *
     * @param object object
     * @return jsonString
     */
    public static <T> String object2Json(T object) {
        return JSON.toJSONString(object);
    }

    /**
     * JSON字符串转换为Java对象
     *
     * @param json json
     * @param obj obj
     * @return T
     */
    public static <T> T json2Object(String json, Class<T> obj) {
        JSONObject jsonObject = JSON.parseObject(json);
        return JSON.toJavaObject(jsonObject, obj);
    }

    /**
     * List集合转换为JSON字符串
     *
     * @param list list
     * @return json
     */
    public static <T> String list2Json(List<T> list) {
        return JSONArray.toJSONString(list);
    }

    /**
     * 将JSON字符串转换为List集合
     *
     * @param json json
     * @param obj obj
     * @return list
     */
    public static <T> List<T> json2List(String json, Class<T> obj) {
        return JSON.parseArray(json, obj);
    }
}
