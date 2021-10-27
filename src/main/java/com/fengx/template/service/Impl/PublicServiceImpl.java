package com.fengx.template.service.Impl;

import com.fengx.template.dao.sys.UserDAO;
import com.fengx.template.dao.sys.UserRoleDAO;
import com.fengx.template.pojo.entity.sys.User;
import com.fengx.template.pojo.entity.sys.UserRole;
import com.fengx.template.service.PublicService;
import com.fengx.template.utils.JSONUtils;
import com.fengx.template.utils.RedisUtils;
import com.fengx.template.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            // 获取权限
            user.setAuthorities(getAuthorities(user.getId()));
            // 缓存
            redisUtils.set(user.getUsername(), JSONUtils.object2Json(user), TokenUtils.JWT_EXPIRATION_TIME, TimeUnit.SECONDS);
            return user;
        } else {
            throw new BadCredentialsException("账号或密码错误！");
        }
    }

    /**
     * 获取用户角色权限
     *
     * @return authorities
     */
    public Collection<GrantedAuthority> getAuthorities(String userId) {
//        Map<String, Role> roleMap = roleDAO.findAll().stream().collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
        List<UserRole> userRoles = userRoleDAO.findAllByUserId(userId);
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // 用户角色 注意：必须"ROLE_"开头
        for(UserRole userRole : userRoles) {
//            Role role = roleMap.get(userRole.getRoleId());
//            if (role != null) {
//                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getId()));
//            }
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRoleId()));
        }
        return grantedAuthorities;
    }
}
