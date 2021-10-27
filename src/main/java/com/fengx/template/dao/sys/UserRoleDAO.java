package com.fengx.template.dao.sys;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.sys.UserRole;

import java.util.List;

public interface UserRoleDAO extends BaseDAO<UserRole> {

    List<UserRole> findAllByUserId(String userId);
}
