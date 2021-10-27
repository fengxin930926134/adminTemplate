package com.fengx.template.dao.sys;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.sys.Role;

public interface RoleDAO extends BaseDAO<Role> {

    boolean existsByName(String name);
}
