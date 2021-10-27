package com.fengx.template.pojo.entity;

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
//仅更新对象（o）中修改过且有值的字段
@DynamicUpdate
public class SysSource extends BaseEntity {

    /**
     * 文件名称包括后缀
     */
    @Column(nullable = false)
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
    @Column(nullable = false)
    private String filePath;
}
