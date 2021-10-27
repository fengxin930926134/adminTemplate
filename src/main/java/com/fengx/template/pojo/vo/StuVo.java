package com.fengx.template.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StuVo {

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private int sex;

    /**
     * 高考号
     */
    private String sno;

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
     * 缴费
     * 组装成String
     */
    private String pays;

    /**
     * 进度名
     * 组装成String
     */
    private String proNames;

    /**
     * 宿舍信息
     */
    private String dorMsg;
}
