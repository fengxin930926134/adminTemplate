package com.fengx.template.dao.sys;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.sys.User;
import java.util.Collection;
import java.util.List;

public interface UserDAO extends BaseDAO<User> {

    User findFirstByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
}
