package com.fengx.template.pojo.vo;

import lombok.Data;

/**
 * 宿舍Vo
 */
@Data
public class DorVo {

    private String id;

    /**
     * 宿舍编号
     * ps: A3-410
     */
    private String dno;

    /**
     * 楼层
     * ps: 4
     */
    private int floor;

    /**
     * 宿舍类型
     * 0 女寝
     * 1 男寝
     */
    private int type;

    /**
     * 位置id
     */
    private String posId;

    /**
     * 位置名称
     */
    private String posName;

    /**
     * 可容纳人数
     */
    private int holdNum;

    /**
     * 当前容纳人数
     */
    private int currentHoldNum;
}
