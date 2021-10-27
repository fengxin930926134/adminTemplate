package com.fengx.template.config.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.fengx.template.exception.NotLoginException;
import com.fengx.template.service.sys.PermissionService;
import com.fengx.template.utils.RequestHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 过滤url
 */
@Slf4j
@RequiredArgsConstructor
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final @NonNull PermissionService permissionService;

    /**
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法。
     * <p>
     * 目的 返回此Url所需要权限 返回null表示不需要权限
     * object-->FilterInvocation
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest request = filterInvocation.getHttpRequest();

        // 检查是否属于不需要登录的url
        if (isMatcherAllowedRequest(filterInvocation)) {
            return null;
        }

        // 接下来的操作都需要登录
        if (RequestHolder.currentUser() == null) {
            log.info("拦截未登录请求: " + request.getRequestURI());
            throw new NotLoginException();
        }

        // 匹配到url需要的权限. 则返回给decide方法进行处理 判断登录用户是否拥有此权限
        String resUrl;
        AntPathRequestMatcher matcher;
        Map<String, Collection<ConfigAttribute>> map = permissionService.getPermissionMap();
        for (String s : map.keySet()) {
            resUrl = s;
            matcher = new AntPathRequestMatcher(resUrl);
            if (matcher.matches(request)) {
                // 返回这个url所需要的权限
                return map.get(resUrl);
            }
        }

        // 不需要权限的 但需要登录的url
        return null;
    }

    /**
     * 判断当前请求是否在允许请求的范围内
     *
     * @param fi 当前请求
     * @return 是否在范围中
     */
    private boolean isMatcherAllowedRequest(FilterInvocation fi) {
        return allowedRequest().stream().map(AntPathRequestMatcher::new)
                .filter(requestMatcher -> requestMatcher.matches(fi.getHttpRequest()))
                .toArray().length > 0;
    }

    /**
     * @return 定义允许请求的列表
     */
    private List<String> allowedRequest() {
        return permissionService.getFilterUrlList();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}