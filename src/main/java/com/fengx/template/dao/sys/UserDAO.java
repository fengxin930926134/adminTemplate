package com.fengx.template.dao.sys;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.sys.User;

public interface UserDAO extends BaseDAO<User> {

    User findFirstByUsernameAndPassword(String username, String password);
}
