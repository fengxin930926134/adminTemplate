package com.fengx.template.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * url对应角色权限
 */
@Data
@Entity
@Table(name = "sys_filter_role")
@DynamicUpdate
public class SysFilterRole {

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    private String id;
    private String filterId;
    private String roleId;
}
