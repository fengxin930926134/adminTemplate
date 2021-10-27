package com.fengx.template.pojo.entity;

import com.fengx.template.base.BaseEntity;
import com.fengx.template.pojo.enums.UserTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 用作测试
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "test")
//仅更新对象（o）中修改过且有值的字段
@DynamicUpdate
public class Test extends BaseEntity {

    @NotNull(message = "用户类型不能为空")
    private UserTypeEnum userTypeEnum;
}
