package com.fengx.template.pojo.vo;

import lombok.Data;

@Data
public class StuPosVo {

    private String id;

    /**
     * 进度名
     */
    private String proName;

    /**
     * 位置名
     */
    private String posName;

    /**
     * 经度
     */
    private double latitude;

    /**
     * 纬度
     */
    private double altitude;
}
