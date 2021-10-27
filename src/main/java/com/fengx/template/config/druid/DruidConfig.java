package com.fengx.template.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接池配置
 */
@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource getDataSource(){
        return new DruidDataSource();
    }

    /**
     * 配置Druid监控
     * 配置一个管理后台的Servlet
     * web监控的filter不需要配置
     */
    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet(){
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams=new HashMap<>();
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "123456");
        // 默认就是允许所有访问
        initParams.put("allow", "127.0.0.1");
        // initParams.put("deny", "127.0.0.1"); // 拒绝哪个网址访问，优先级大于allow 可以让别人无法通过此Ip访问
        bean.setInitParameters(initParams);
        return bean;
    }
}