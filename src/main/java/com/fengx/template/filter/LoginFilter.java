package com.fengx.template.filter;

import com.fengx.template.exception.TokenException;
import com.fengx.template.pojo.entity.sys.User;
import com.fengx.template.utils.common.JSONUtils;
import com.fengx.template.utils.common.RedisUtils;
import com.fengx.template.utils.common.RequestHolder;
import com.fengx.template.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录过滤器
 */
@RequiredArgsConstructor
public class LoginFilter extends GenericFilterBean {

    private final @NonNull RedisUtils redisUtils;
    // 用作抛出全局异常能收到的异常
    private final @NonNull HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            String authToken = request.getHeader(TokenUtils.HEADER_STRING);
            if (StringUtils.hasText(authToken)) {
                // 解析 Token
                Claims claims = TokenUtils.parseToken(authToken);
                if (claims == null) {
                    throw new TokenException();
                }
                // 可能伪造
                String username = (String) claims.get(TokenUtils.JWT_LOGIN_USERNAME);
                if (username == null) {
                    throw new TokenException();
                }
                // 判断过期
                String user = redisUtils.get(username);
                if (user == null) {
                    throw new TokenException();
                }
                User sysUser = JSONUtils.json2Object(user, User.class);
                // 得到权限（角色）
                Authentication auth = new UsernamePasswordAuthenticationToken(sysUser.getUsername(),
                        sysUser.getPassword(), TokenUtils.getAuthorities(sysUser.getRoleIds()));
                // 这里还是上面见过的，存放认证信息，如果没有走这一步，下面的doFilter就会提示登录了
                SecurityContextHolder.getContext().setAuthentication(auth);
                RequestHolder.add(sysUser);
            }
            // Security的路径拦截是在这个之后 调用后续的Filter,如果上面的代码逻辑未能复原“session”，SecurityContext中没有想过信息，后面的流程会检测出"需要登录"
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, e);
        }
    }
}
