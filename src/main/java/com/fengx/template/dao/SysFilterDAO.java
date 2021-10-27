package com.fengx.template.dao;

import com.fengx.template.base.BaseDAO;
import com.fengx.template.pojo.entity.SysFilter;

import java.util.List;

public interface SysFilterDAO extends BaseDAO<SysFilter> {

    List<SysFilter> findAllByStatusAndType(int status, int type);
}
