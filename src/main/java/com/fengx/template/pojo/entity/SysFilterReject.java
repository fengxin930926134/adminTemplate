package com.fengx.template.pojo.entity;

import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统url过滤剔除列表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_filter_reject")
@DynamicUpdate
public class SysFilterReject extends BaseEntity {

    /**
     * 剔除链接
     * 注意：url通配符为两颗星，比如说 /user下的所有url，应该写成 /user/**;
     */
    @Column(nullable = false)
    private String url;

    /**
     * 描述
     */
    @Column(name = "`desc`")
    private String desc;

    /**
     * 状态
     * 0.禁用
     * 1.启用
     */
    @Column(nullable = false, columnDefinition="tinyint default 0")
    private int status;
}
