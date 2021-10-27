package com.fengx.template.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
// ApiModel只在参数生效
@ApiModel(value = "添加用户参数")
public class UserAddParam {

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型", notes = "0.管理员 1.学生")
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer sex;

    /**
     * 高考号
     */
    @ApiModelProperty(value = "高考号")
    @NotBlank(message = "高考号/账号不能为空")
    private String sno;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idCard;

    /**
     * 毕业学校
     */
    @ApiModelProperty(value = "毕业学校")
    private String school;

    /**
     * 高考成绩
     */
    @ApiModelProperty(value = "高考成绩")
    private Double hkale;

    /**
     * 毕业时间
     */
    @ApiModelProperty(value = "毕业时间")
    private LocalDateTime graduationTime;
}
