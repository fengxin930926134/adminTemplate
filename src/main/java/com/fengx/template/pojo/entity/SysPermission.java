package com.fengx.template.pojo.entity;

import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.util.List;

/**
 * 系统权限表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_permission")
//仅更新对象（o）中修改过且有值的字段
@DynamicUpdate
public class SysPermission extends BaseEntity {

    /**
     * 权限描述
     */
    @Column(name = "`desc`")
    private String desc;

    /**
     * 授权链接
     * 注意：url通配符为两颗星，比如说 /user下的所有url，应该写成 /user/**;
     */
    @Column(nullable = false)
    private String url;

    /**
     * 状态
     * 0.禁用
     * 1.启用
     */
    @Column(nullable = false, columnDefinition="tinyint default 0")
    private int status;

    /**
     * 角色
     * 权限 - 角色是多对多的关系
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            // 表名
            name = "sys_permission_role",
            // 当前id
            joinColumns = { @JoinColumn(name = "permission_id") },
            // 连接表的id
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private List<SysRole> roles;
}
