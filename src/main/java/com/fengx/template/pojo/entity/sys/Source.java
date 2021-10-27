package com.fengx.template.pojo.entity.sys;

import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统资源表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_source")
@DynamicUpdate
public class Source extends BaseEntity {

    /**
     * 文件名称包括后缀
     */
    private String fileName;

    /**
     * 文件大小 Byte（字节）
     */
    private Long fileSize;

    /**
     * 文件类型 (png, mp4...)
     */
    private String fileType;

    /**
     * 文件位置 (包括实际名称)
     */
    private String filePath;
}
