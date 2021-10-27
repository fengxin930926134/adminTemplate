package com.fengx.template.config.security;

import com.fengx.template.dao.SysSourceDao;
import com.fengx.template.dao.SysUserDao;
import com.fengx.template.pojo.entity.SysUser;
import com.fengx.template.utils.RedisUtils;
import com.fengx.template.utils.TokenUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.concurrent.TimeUnit;

/**
 * 自定义身份认证验证组件
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final @NonNull RedisUtils redisUtils;
    private final @NonNull SysUserDao sysUserDao;
    private final @NonNull PasswordEncoder passwordEncoder;
    private final @NonNull SysSourceDao sysSourceDao;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticate: user info authentication");
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 认证逻辑
        SysUser user = sysUserDao.findFirstByUsernameAndPassword(name, passwordEncoder.encode(password));
        if (user != null) {
            if (user.getPictureId() != null) {
                sysSourceDao.findById(user.getPictureId()).ifPresent(source -> user.setFace(source.getFilePath()));
            }
            // 缓存1天
            redisUtils.set(user.getUsername(), user, TokenUtils.JWT_EXPIRATION_TIME, TimeUnit.SECONDS);
            // 生成令牌
            return new UsernamePasswordAuthenticationToken(name, password, user.getAuthorities());
        }else {
            throw new BadCredentialsException("账号或密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 是否可以提供输入类型的认证服务
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}