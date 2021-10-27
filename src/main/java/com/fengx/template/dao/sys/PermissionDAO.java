package com.fengx.template.dao.sys;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.sys.Permission;

import java.util.List;

public interface PermissionDAO extends BaseDAO<Permission> {

    List<Permission> findAllByStatusAndType(int status, int type);
}
