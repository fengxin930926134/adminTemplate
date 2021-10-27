package com.fengx.template.config.security;

import com.fengx.template.config.encode.PasswordEncoderConfig;
import com.fengx.template.dao.SysSourceDao;
import com.fengx.template.dao.SysUserDao;
import com.fengx.template.filter.LoginFilter;
import com.fengx.template.pojo.entity.SysUser;
import com.fengx.template.service.sys.SysPermissionService;
import com.fengx.template.utils.RedisUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//开启方法级别验证 prePostEnabled方法之前
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final @NonNull SysUserDao sysUserDao;
    private final @NonNull RedisUtils<SysUser> redisUtils;
    private final @NonNull HandlerExceptionResolver handlerExceptionResolver;
    private final @NonNull SysPermissionService sysPermissionService;
    private final @NonNull SysSourceDao sysSourceDao;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf验证
        http.csrf().disable()
            .authorizeRequests()
            // 自定义url过滤 鉴权
            .withObjectPostProcessor(new CustomObjectPostProcessor())
            .anyRequest().authenticated()  // 任何请求,登录后可以访问
            // 扩展access()的SpEL表达式实现URL动态权限 .anyRequest().access("@authService.canAccess(request, authentication)")
            // 不创建不使用session
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 添加一个过滤器验证Token是否合法，是否需要登录
            .addFilterBefore(new LoginFilter(redisUtils, handlerExceptionResolver), UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // 自定义身份认证验证组件
        auth.authenticationProvider(new CustomAuthenticationProvider(redisUtils, sysUserDao, passwordEncoder(), sysSourceDao));
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoderConfig();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
        return new CustomFilterInvocationSecurityMetadataSource(sysPermissionService);
    }

    @Bean
    public CustomAccessDecisionManager accessDecisionManager() {
        return new CustomAccessDecisionManager();
    }

    private class CustomObjectPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor> {
        @Override
        public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
            fsi.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
            fsi.setAccessDecisionManager(accessDecisionManager());
            return fsi;
        }

    }
}