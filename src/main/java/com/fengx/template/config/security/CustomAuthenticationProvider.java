package com.fengx.template.config.security;

import com.fengx.template.pojo.entity.sys.User;
import com.fengx.template.service.PublicService;
import com.fengx.template.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义身份认证验证组件
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PublicService publicService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authenticate: user info authentication");
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 认证逻辑
        User user = publicService.authentication(name, passwordEncoder.encode(password));
        // 生成令牌
        return new UsernamePasswordAuthenticationToken(name, password, TokenUtils.getAuthorities(user.getRoleIds()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 是否可以提供输入类型的认证服务
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}