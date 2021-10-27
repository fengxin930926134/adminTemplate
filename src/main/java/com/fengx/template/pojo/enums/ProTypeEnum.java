package com.fengx.template.pojo.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报到流程类型
 */
@Getter
public enum ProTypeEnum {
    DEFAULT(1, "固定位置,根据地址直接前往"),
    DORMITORY(2, "宿舍位置,根据分配用户宿舍地址前往");

    private final int code;
    private final String message;

    ProTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static List<Map<String, Object>> getAll() {
        List<Map<String, Object>> values = new ArrayList<>();
        for (ProTypeEnum type : ProTypeEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", type.getCode());
            map.put("message", type.getMessage());
            values.add(map);
        }
        return values;
    }
}