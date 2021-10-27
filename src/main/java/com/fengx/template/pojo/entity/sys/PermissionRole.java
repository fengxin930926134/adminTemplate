package com.fengx.template.pojo.entity.sys;

import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色权限关联
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_permission_role")
@DynamicUpdate
public class PermissionRole extends BaseEntity {

    /**
     * 权限Id
     */
    private String permissionId;

    /**
     * 角色id
     */
    private String roleId;
}
