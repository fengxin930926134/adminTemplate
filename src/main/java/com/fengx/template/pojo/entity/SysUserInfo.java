package com.fengx.template.pojo.entity;

import com.fengx.template.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 系统用户信息表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user_info")
//仅更新对象（o）中修改过且有值的字段
@DynamicUpdate
public class SysUserInfo extends BaseEntity {

    /**
     * 性别
     * 0.女
     * 1.男
     */
    @Column(nullable = false, columnDefinition="tinyint default 0")
    private int sex;

    /**
     * 高考号
     */
    @Column(nullable = false)
    private String sno;

    /**
     * 身份证号
     */
    @Column(nullable = false)
    private String idCard;

    /**
     * 毕业学校
     */
    @Column(nullable = false)
    private String school;

    /**
     * 高考成绩
     */
    @Column(nullable = false)
    private Double hkale;

    /**
     * 毕业时间
     */
    @Column(nullable = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime graduationTime;
}
