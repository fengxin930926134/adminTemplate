package com.fengx.template.pojo.entity.sys;

import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 权限表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_permission")
@DynamicUpdate
public class Permission extends BaseEntity {

    /**
     * url通配符为两颗星，比如说 /user下的所有url，应该写成 /user/**;
     */
    private String url;

    /**
     * 描述
     */
    private String describe;

    /**
     * 状态
     * 0.禁用
     * 1.启用
     */
    private int status;

    /**
     * url类型
     * 1.拦截
     * 2.访问权限
     */
    private int type;
}
