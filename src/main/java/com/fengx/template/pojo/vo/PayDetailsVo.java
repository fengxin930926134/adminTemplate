package com.fengx.template.pojo.vo;

import lombok.Data;

/**
 * 支付详情Vo
 */
@Data
public class PayDetailsVo {

    private String id;

    /**
     * 关联用户id
     */
    private String userId;

    /**
     * 关联用户名
     */
    private String userName;

    /**
     * 缴费类别id
     */
    private String costId;

    /**
     * 缴费类别名
     */
    private String costName;

    /**
     * 缴费费用
     */
    private double cost;

    /**
     * 是否缴费
     * 1.是
     * -1.否
     */
    private int pay;
}
