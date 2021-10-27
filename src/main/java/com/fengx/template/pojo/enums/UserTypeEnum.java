package com.fengx.template.pojo.enums;

import com.fengx.template.exception.WarnException;
import lombok.Getter;

/**
 * 用户类型
 */
@Getter
public enum UserTypeEnum {
    /**
     * 超管员
     */
    ADMIN(0, "超管员"),
    /**
     * 普通用户
     */
    STUDENT(1, "普通用户");

    private final int code;
    private final String message;

    UserTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    /**
     * 检查值是否正确
     * @param value code
     */
    public static int checkUserType(int value) {
        for (UserTypeEnum type : UserTypeEnum.values()) {
            if (type.getCode() == value) {
                return type.getCode();
            }
        }
        throw new WarnException("用户类型错误");
    }
}