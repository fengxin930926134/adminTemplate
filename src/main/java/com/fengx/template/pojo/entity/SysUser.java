package com.fengx.template.pojo.entity;

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
//仅更新对象（o）中修改过且有值的字段
@DynamicUpdate
@NoArgsConstructor
public class SysUser extends BaseEntity {

    /**
     * 用户名
     */
    @Column(nullable = false)
    private String name;

    /**
     * 账号 / 高考号
     */
    @Column(nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 用户类型
     * 0.管理员
     * 1.学生
     * 用枚举类UserTypeEnumCode值
    */
    @Column(nullable = false)
    private int userType;

    /**
     * 用户信息id
     */
    private String userInfoId;

    /**
     * 头像id
     */
    private String pictureId;

    /**
     * 头像地址
     */
    @Transient
    private String face;

    /**
     * 角色
     * 用户－角色：多对多的关系．
     */
    @ManyToMany(fetch = FetchType.EAGER) //立即从数据库中进行加载数据;
    @JoinTable(
            // 表名
            name = "sys_user_role",
            // 当前id
            joinColumns = { @JoinColumn(name = "user_id") },
            // 连接表的id
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private List<SysRole> roles;

    /**
     * 加权
     * 获取用户角色拥有的权限
     * @return authorities
     */
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // 用户可以访问的资源名称 注意：必须"ROLE_"开头
        for(SysRole sysRole: roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + sysRole.getName()));
        }
        return grantedAuthorities;
    }

}
