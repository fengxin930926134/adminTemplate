package com.fengx.template.dao;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.SysRole;

public interface SysRoleDAO extends BaseDAO<SysRole> {

    boolean existsByName(String name);

    SysRole findByName(String name);
}
