package com.fengx.template.service.sys;

import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PermissionService {

    /**
     * 动态获取角色权限Map
     *
     * @return Map
     */
    Map<String, Collection<ConfigAttribute>> getPermissionMap();

    /**
     * 动态获取拦截器放过url列表
     *
     * @return List
     */
    List<String> getFilterUrlList();
}
