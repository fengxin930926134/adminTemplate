package com.fengx.template.pojo.vo;

import com.fengx.template.pojo.entity.sys.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息vo
 */
@Data
public class UserVo {

    private String id;

    /**
     * 用户信息id
     */
    private String userInfoId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户类型
     * 0.管理员
     * 1.学生
     */
    private int userType;

    /**
     * 性别
     */
    private int sex;

    /**
     * 高考号
     */
    private String sno;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 毕业学校
     */
    private String school;

    /**
     * 高考成绩
     */
    private Double hkale;

    /**
     * 毕业时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime graduationTime;

    /**
     * 头像地址
     */
    private String filePath;

    /**
     * 角色
     * 用户－角色：多对多的关系．
     */
    private List<Role> roles;
}
