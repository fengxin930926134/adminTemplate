package com.fengx.template.config.security;

import java.util.Collection;
import com.fengx.template.exception.PermissionException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 鉴权
 */
@RequiredArgsConstructor
public class CustomAccessDecisionManager implements AccessDecisionManager{

    /**
     * 方法是判定是否拥有权限的决策方法，
     * (1)authentication 是释CustomUserService中循环添加到 GrantedAuthority 对象中的权限信息集合.
     * (2)object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
     * (3)configAttributes 为MyFilterInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法
     */
    @SneakyThrows
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        if(configAttributes == null  || configAttributes.size() == 0) {
            throw new PermissionException();
        }

        //遍历基于URL获取的权限信息和用户自身的角色信息进行对比.
        ConfigAttribute cfa;
        String needRole;
        for (ConfigAttribute configAttribute : configAttributes) {
            cfa = configAttribute;
            needRole = cfa.getAttribute();
            //authentication 为CustomUserDetailService中添加的权限信息.
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (needRole.equals(grantedAuthority.getAuthority())) {
                    return;
                }
            }
        }

        throw new PermissionException();
    }



    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}