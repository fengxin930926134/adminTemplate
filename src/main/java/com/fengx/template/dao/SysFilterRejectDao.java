package com.fengx.template.dao;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.SysFilterReject;

import java.util.List;

public interface SysFilterRejectDao extends BaseDAO<SysFilterReject> {

    List<SysFilterReject> findByStatus(int status);
}
