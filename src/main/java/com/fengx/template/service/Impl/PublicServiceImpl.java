package com.fengx.template.service.Impl;

import com.fengx.template.dao.sys.UserDAO;
import com.fengx.template.dao.sys.UserRoleDAO;
import com.fengx.template.pojo.entity.sys.User;
import com.fengx.template.pojo.entity.sys.UserRole;
import com.fengx.template.service.PublicService;
import com.fengx.template.utils.common.JSONUtils;
import com.fengx.template.utils.common.RedisUtils;
import com.fengx.template.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {

    private final UserDAO userDAO;
    private final UserRoleDAO userRoleDAO;
    private final RedisUtils redisUtils;

    @Override
    public User authentication(String username, String password) {
        User user = userDAO.findFirstByUsernameAndPassword(username, password);
        if (user != null) {
            // 获取用户角色
            List<UserRole> roles = userRoleDAO.findAllByUserId(user.getId());
            user.setRoleIds(roles.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
            // 缓存
            redisUtils.set(user.getUsername(), JSONUtils.object2Json(user), TokenUtils.JWT_EXPIRATION_TIME, TimeUnit.SECONDS);
            return user;
        } else {
            throw new BadCredentialsException("账号或密码错误！");
        }
    }

}
