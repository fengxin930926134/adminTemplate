package com.fengx.template.pojo.entity.sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengx.template.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 系统用户表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user")
@DynamicUpdate
@NoArgsConstructor
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String name;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户角色权限
     */
    @Transient
    @JsonIgnore
    private Collection<GrantedAuthority> authorities;
}
