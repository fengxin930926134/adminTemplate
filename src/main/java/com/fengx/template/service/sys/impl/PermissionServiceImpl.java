package com.fengx.template.service.sys.impl;

import com.fengx.template.dao.sys.PermissionDAO;
import com.fengx.template.dao.sys.PermissionRoleDAO;
import com.fengx.template.pojo.entity.sys.Permission;
import com.fengx.template.pojo.entity.sys.PermissionRole;
import com.fengx.template.service.sys.PermissionService;
import com.fengx.template.utils.common.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionDAO permissionDAO;
    private final PermissionRoleDAO permissionRoleDAO;

    /**
     * url需要权限map缓存
     */
    private static Map<String, Collection<ConfigAttribute>> permissionMap = null;


    @PostConstruct
    private void initPermissions() {
        log.info("initPermissions");
        permissionMap = new HashMap<>(1);
        // 只获取启用的
        List<Permission> permissions = permissionDAO.findAllByStatusAndType(1, 2);
        // 权限对应角色
        Map<String, List<PermissionRole>> permissionRoleMap = permissionRoleDAO.findAll().stream().collect(Collectors.groupingBy(PermissionRole::getPermissionId));
        for (Permission permission : permissions) {
            List<PermissionRole> permissionRoles = permissionRoleMap.get(permission.getId());
            if (CollectionUtils.isNotEmpty(permissionRoles)) {
                Collection<ConfigAttribute> collection = new ArrayList<>();
                for (PermissionRole r : permissionRoles) {
                    ConfigAttribute cfg = new SecurityConfig("ROLE_" + r.getRoleId());
                    collection.add(cfg);
                }
                permissionMap.put(permission.getUrl(), collection);
            }
        }
    }

    @Override
    public Map<String, Collection<ConfigAttribute>> getPermissionMap() {
        if (ObjectUtils.checkValue(permissionMap)) {
            initPermissions();
        }
        return permissionMap;
    }

    @Override
    public List<String> getFilterUrlList() {
        // 只获取启用的
        List<Permission> permissions = permissionDAO.findAllByStatusAndType(1, 1);
        return permissions.stream().map(Permission::getUrl).distinct().collect(Collectors.toList());
    }
}
