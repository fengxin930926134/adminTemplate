package com.fengx.template.service.sys;

import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.AuthAddParam;
import com.fengx.template.pojo.param.AuthUpdateParam;
import com.fengx.template.pojo.param.OnOffParam;
import com.fengx.template.response.Response;
import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SysPermissionService {

    /**
     * 动态获取路径对应权限Map
     *
     * @return Map
     */
    Map<String, Collection<ConfigAttribute>> getPermissionMap();

    /**
     * 动态获取拦截器放过url列表
     *
     * @return List
     */
    List<String> getFilterUrlList();

    /**
     * 分页查询权限列表
     *
     * @param param PageParam
     * @return PageData
     */
    Response<?> page(PageParam param);

    /**
     * 添加权限
     *
     * @param param AuthAddParam
     * @return Response
     */
    Response<?> add(AuthAddParam param);

    /**
     * 更新权限
     *
     * @param param AuthUpdateParam
     * @return Response
     */
    Response<?> update(AuthUpdateParam param);

    /**
     * 启用或禁用权限
     *
     * @param param OnOffParam
     * @return Response
     */
    Response<?> onOff(OnOffParam param);
}
