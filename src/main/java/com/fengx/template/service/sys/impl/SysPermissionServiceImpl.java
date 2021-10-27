package com.fengx.template.service.sys.impl;

import com.fengx.template.base.BaseEntity;
import com.fengx.template.dao.SysFilterDAO;
import com.fengx.template.dao.SysFilterRoleDAO;
import com.fengx.template.dao.SysRoleDAO;
import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.AuthAddParam;
import com.fengx.template.pojo.param.AuthUpdateParam;
import com.fengx.template.pojo.param.OnOffParam;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.SysPermissionService;
import com.fengx.template.utils.common.ObjectUtils;
import com.fengx.template.utils.RedisUtils;
import com.fengx.template.pojo.entity.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final @NonNull RedisUtils redisUtils;
    private final @NonNull SysRoleDAO roleDAO;
    private final SysFilterDAO filterDAO;
    private final SysFilterRoleDAO filterRoleDAO;

    /**
     * url需要权限map缓存
     */
    private static Map<String, Collection<ConfigAttribute>> permissionMap = null;


    @PostConstruct
    private void initPermissions() {
        log.info("initPermissions");
        permissionMap = new HashMap<>(1);
        // 只获取启用的
        List<SysFilter> sysFilters = filterDAO.findAllByStatusAndType(1, 2);
        // 装载路径对应权限
        Map<String, List<SysFilterRole>> filterRoleMap = filterRoleDAO.findAll().stream().collect(Collectors.groupingBy(SysFilterRole::getFilterId));
        Map<String, SysRole> roleMap = roleDAO.findAll().stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
        for (SysFilter filter : sysFilters) {
            // 该路径对应角色
            List<SysFilterRole> sysFilterRoles = filterRoleMap.get(filter.getId());
            if (CollectionUtils.isNotEmpty(sysFilterRoles)) {
                Collection<ConfigAttribute> collection = new ArrayList<>();
                for (SysFilterRole r : sysFilterRoles) {
                    SysRole sysRole = roleMap.get(r.getRoleId());
                    if (sysRole != null) {
                        ConfigAttribute cfg = new SecurityConfig("ROLE_" + sysRole.getName());
                        collection.add(cfg);
                    }
                }
                permissionMap.put(filter.getUrl(), collection);
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
        List<SysFilter> sysFilters = filterDAO.findAllByStatusAndType(1, 1);
        return sysFilters.stream().map(SysFilter::getUrl).distinct().collect(Collectors.toList());
    }

    @Override
    public Response<?> page(PageParam param) {
        return Response.success(filterDAO.findAll(param));
    }

    @Override
    public Response<?> add(AuthAddParam param) {
        if (param.getType() == 1) {
            SysFilterReject sfr = ObjectUtils.copy(param, new SysFilterReject());
            sfr.setStatus(1);
            sysFilterRejectDao.saveAndFlush(sfr);
            // 刷新缓存
            stringRedisUtils.delete(stringRedisUtils.REDIS_KEY_FILTER_REJECT_LIST);
        } else if (param.getType() == 2) {
            SysPermission sp = ObjectUtils.copy(param, new SysPermission());
            sp.setStatus(2);
            // 添加的可访问角色
            sp.setRoles(getRoles(param.getRoleIds()));
            sysPermissionDao.saveAndFlush(sp);
            // 刷新缓存
            redisUtils.delete(redisUtils.REDIS_KEY_PERMISSION_LIST);
            permissionMap.clear();
        } else {
            return Response.failed("权限类型错误");
        }
        return Response.success();
    }

    @Override
    public Response<?> update(AuthUpdateParam param) {
        if (param.getType() == 1) {
            SysFilterReject sfr = sysFilterRejectDao.findById(param.getId()).orElse(null);
            if (sfr != null) {
                SysFilterReject sfrNew = ObjectUtils.copy(param, sfr);
                sfrNew.setStatus(1);
                sysFilterRejectDao.saveAndFlush(sfr);
                // 刷新缓存
                stringRedisUtils.delete(stringRedisUtils.REDIS_KEY_FILTER_REJECT_LIST);
            }
        } else if (param.getType() == 2) {
            SysPermission sp = sysPermissionDao.findById(param.getId()).orElse(null);
            if (sp != null) {
                SysPermission spNew = ObjectUtils.copy(param, sp);
                spNew.setStatus(2);
                // 添加的可访问角色
                sp.setRoles(getRoles(param.getRoleIds()));
                sysPermissionDao.saveAndFlush(sp);
                // 刷新缓存
                redisUtils.delete(redisUtils.REDIS_KEY_PERMISSION_LIST);
                permissionMap.clear();
            }
        } else {
            return Response.failed("权限类型错误");
        }
        return Response.success();
    }

    @Override
    public Response<?> onOff(OnOffParam param) {
        SysPermission sp = sysPermissionDao.findById(param.getId()).orElse(null);
        if (sp == null) {
            SysFilterReject sfr = sysFilterRejectDao.findById(param.getId()).orElse(null);
            if (sfr != null) {
                if (sfr.getStatus() == param.getEnable()) {
                    return Response.success();
                }
                sfr.setStatus(param.getEnable() == 1 ? param.getEnable() : 0);
                sysFilterRejectDao.save(sfr);
                // 刷新缓存
                stringRedisUtils.delete(stringRedisUtils.REDIS_KEY_FILTER_REJECT_LIST);
                return Response.success();
            }
        } else {
            if (sp.getStatus() == param.getEnable()) {
                return Response.success();
            }
            sp.setStatus(param.getEnable() == 1 ? param.getEnable() : 0);
            sysPermissionDao.save(sp);
            // 刷新缓存
            redisUtils.delete(redisUtils.REDIS_KEY_PERMISSION_LIST);
            return Response.success();
        }
        return Response.failed();
    }

    /**
     * 根据ids获取权限信息
     *
     * @param ids 权限id
     * @return LIST
     */
    private List<SysRole> getRoles(List<String> ids) {
        Set<SysRole> roles = Sets.newHashSet();
        List<SysRole> all = sysRoleDao.findAll();
        for (SysRole role : all) {
            if (ids.contains(role.getId())) {
                roles.add(role);
            }
        }
        return Lists.newArrayList(roles);
    }
}
