package com.fengx.template.service.sys.impl;

import com.fengx.template.dao.SysRoleDAO;
import com.fengx.template.pojo.entity.SysRole;
import com.fengx.template.pojo.page.PageData;
import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.IdsParam;
import com.fengx.template.pojo.param.RoleAddParam;
import com.fengx.template.pojo.param.RoleUpdateParam;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.SysRoleService;
import com.fengx.template.utils.common.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final @NonNull SysRoleDAO sysRoleDao;

    @Override
    public Response<?> page(PageParam param) {
        return Response.success(new PageData(sysRoleDao.findAll(param)));
    }

    @Override
    public Response<?> add(RoleAddParam param) {
        if (sysRoleDao.existsByName(param.getName())) {
            return Response.failed("所添加角色已存在");
        }
        SysRole copy = ObjectUtils.copy(param, new SysRole());
        sysRoleDao.saveAndFlush(copy);
        return Response.success();
    }

    @Override
    public Response<?> update(RoleUpdateParam param) {
        SysRole role = sysRoleDao.findById(param.getId()).orElse(null);
        if (role != null) {
            role = ObjectUtils.copy(param, role);
            sysRoleDao.saveAndFlush(role);
        }
        return Response.success();
    }

    @Override
    public Response<?> delete(IdsParam param) {
        List<SysRole> sysRoles = sysRoleDao.findAllById(param.getIds());
        if (ObjectUtils.checkValue(sysRoles)) {
            for (SysRole role: sysRoles) {
                sysRoleDao.delete(role);
            }
        }
        return Response.success();
    }

    @Override
    public Response<?> getAll() {
        return Response.success(sysRoleDao.findAll());
    }
}
