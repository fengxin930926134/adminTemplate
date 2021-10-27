package com.fengx.template.utils.common;

import com.fengx.template.pojo.entity.sys.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 存储本地HTTP请求，当前登录用户
 */
public class RequestHolder {

    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new ThreadLocal<>();

    public static void add(User user) {
        USER_HOLDER.set(user);
    }

    public static void add(HttpServletRequest request) {
        REQUEST_HOLDER.set(request);
    }

    /**
     * 获取当前用户不含密码
     */
    public static User currentUser() {
        return USER_HOLDER.get();
    }

    public static HttpServletRequest currentRequest() {
        return REQUEST_HOLDER.get();
    }

    public static void remove() {
        USER_HOLDER.remove();
        REQUEST_HOLDER.remove();
    }
}
