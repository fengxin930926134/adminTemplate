package com.fengx.template.service.sys;

import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.IdsParam;
import com.fengx.template.pojo.param.RoleAddParam;
import com.fengx.template.pojo.param.RoleUpdateParam;
import com.fengx.template.response.Response;

public interface SysRoleService {

    /**
     * 分页查询角色
     * @param param PageParam
     * @return PageData
     */
    Response<?> page(PageParam param);

    /**
     * 添加角色
     * @param param RoleAddParam
     * @return Response
     */
    Response<?> add(RoleAddParam param);

    /**
     * 更新角色
     * @param param RoleUpdateParam
     * @return Response
     */
    Response<?> update(RoleUpdateParam param);

    /**
     * 删除角色
     * @param param IdsParam
     * @return Response
     */
    Response<?> delete(IdsParam param);

    Response<?> getAll();
}
