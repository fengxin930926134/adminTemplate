package com.fengx.template.service.sys;

import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.IdsParam;
import com.fengx.template.pojo.param.LoginParam;
import com.fengx.template.pojo.param.PwdParam;
import com.fengx.template.pojo.param.UserAddParam;
import com.fengx.template.response.Response;
import org.springframework.web.multipart.MultipartFile;

public interface SysUserService {

    /**
     * 登录
     * 验证账号密码
     * 认证 加权 缓存
     * @param param LoginParam
     * @return LoginVo
     */
    Response<?> login(LoginParam param);

    /**
     * 分页查询用户信息
     * @param param PageParam
     * @return PageData
     */
    Response<?> page(PageParam param);

    /**
     * 添加用户
     * @param param UserAddParam
     * @return Response
     */
    Response<?> add(UserAddParam param);

    /**
     * 删除用户
     * @param param IdsParam
     * @return Response
     */
    Response<?> delete(IdsParam param);

    /**
     * 通过excel导入学生信息
     * @param file excel
     * @return Response
     */
    Response<?> importExcel(MultipartFile file);

    /**
     * 下载学生信息
     * @return Response
     */
    Response<?> downloadExcel();

    /**
     * 修改密码
     * @param pwdParam pwdParam
     * @return Response
     */
    Response<?> upPassword(PwdParam pwdParam);
}
