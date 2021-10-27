package com.fengx.template.response;

import lombok.Getter;

/**
 * 返回状态码枚举类
 */
@Getter
public enum ResponseEnum {
    SUCCESS(200, "操作成功"),
    NOT_LOGIN(401, "未登录"),
    TOKEN_INVALID(402, "token失效"),
    NOT_PERMISSION(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    WARN(405, "警告"),
    FAIL(500, "操作失败");

    private final int code;
    private final String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
