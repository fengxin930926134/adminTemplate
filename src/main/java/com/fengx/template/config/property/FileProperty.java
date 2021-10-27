package com.fengx.template.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件相关属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "source.file")
public class FileProperty {

    /**
     * 所在路径
     */
    private String path;

    /**
     * 映射路径
     */
    private String mapping;
}
