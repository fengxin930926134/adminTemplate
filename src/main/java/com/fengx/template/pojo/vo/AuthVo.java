package com.fengx.template.pojo.vo;

import com.fengx.template.pojo.entity.sys.Role;
import lombok.Data;
import java.util.List;

/**
 * 权限列表返回Vo
 */
@Data
public class AuthVo {

    private String id;

    /**
     * 权限描述
     */
    private String desc;

    /**
     * 授权链接
     */
    private String url;

    /**
     * 权限类型
     * 1.不需要登录的权限 2.url需要角色访问权限
     */
    private int type;

    /**
     * 状态
     * 0.禁用
     * 1.启用
     */
    private int status;

    /**
     * 角色
     * 权限 - 角色是多对多的关系
     */
    private List<Role> roles;
}
