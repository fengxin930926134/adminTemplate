package com.fengx.template.pojo.entity;

import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

/**
 * 系统角色表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_role")
//仅更新对象（o）中修改过且有值的字段
@DynamicUpdate
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 角色描述
     */
    @Column(name = "`desc`")
    private String desc;
}
