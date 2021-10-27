package com.fengx.template.pojo.vo.sys;

import lombok.Data;

/**
 * 登录返回参数
 *
 * 由于返回参数会序列化成返回body的msg字段，@ApiModel不会生效
 */
@Data
public class LoginVO {

    /**
     * 用户名
     */
    private String name;

    private String token;
}
