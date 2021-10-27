package com.fengx.template.config.mvc;

import com.fengx.template.interceptor.HttpInterceptor;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * spring配置类
 */
@Slf4j
@Configuration
@EnableWebMvc
@EnableSwagger2
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${source.file.path}")
    private String fileSavePath;

    @Value("${source.file.mapping}")
    private String sourceMapping;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 配置编码
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter)converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String url = "/**";
        registry.addInterceptor(new HttpInterceptor())
                .addPathPatterns(url)
                // 放行url
                .excludePathPatterns();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射swagger静态资源
        registry.addResourceHandler("/swagger-resources")
                .addResourceLocations("classpath:/static/swagger-resources");
        // 映射swagger-ui
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 映射swagger的js文件
        registry.addResourceHandler("/webjars/springfox-swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        // 静态资源映射
        registry.addResourceHandler(sourceMapping + "/**").addResourceLocations("file:" + fileSavePath + "/");
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为有@Api注解的Controller生成API文档
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("使用 Swagger2 构建 RESTFUL 风格 API 文档")
                .description("无")
                // 服务条例url
                .termsOfServiceUrl("服务条例")
                .contact(new Contact("无",
                        "https://user.qzone.qq.com/930926134",
                        "930926134@qq.com"))
                .version("v1.0")
                .build();
    }
}
