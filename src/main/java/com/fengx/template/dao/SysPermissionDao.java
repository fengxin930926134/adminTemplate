package com.fengx.template.dao;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.SysPermission;
import java.util.List;

public interface SysPermissionDao extends BaseDAO<SysPermission> {

    List<SysPermission> findByStatus(int status);
}
