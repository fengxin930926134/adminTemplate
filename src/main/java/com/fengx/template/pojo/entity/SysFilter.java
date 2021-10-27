package com.fengx.template.pojo.entity;

import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统url过滤
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_filter")
@DynamicUpdate
public class SysFilter extends BaseEntity {

    /**
     * url通配符为两颗星，比如说 /user下的所有url，应该写成 /user/**;
     */
    private String url;

    /**
     * 描述
     */
    private String desc;

    /**
     * 状态
     * 0.禁用
     * 1.启用
     */
    private int status;

    /**
     * url类型
     * 1.拦截
     * 2.需要权限
     */
    private int type;
}
