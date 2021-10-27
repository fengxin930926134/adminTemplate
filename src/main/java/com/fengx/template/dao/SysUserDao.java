package com.fengx.template.dao;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.SysUser;
import java.util.Collection;
import java.util.List;

public interface SysUserDao extends BaseDAO<SysUser> {

    SysUser findFirstByUsernameAndPassword(String username, String password);

    List<SysUser> findAllByUserType(int userType);

    boolean existsByUsername(String username);

    List<SysUser> findAllByNameLike(String name);

    List<SysUser> findAllByUserTypeAndIdNotIn(int userType, Collection<String> id);
}
