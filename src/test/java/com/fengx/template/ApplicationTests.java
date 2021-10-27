package com.fengx.template;

import com.fengx.template.dao.*;
import com.fengx.template.pojo.entity.*;
import com.fengx.template.pojo.enums.UserTypeEnum;
import com.fengx.template.pojo.page.Operator;
import com.fengx.template.pojo.page.SearchFilter;
import com.fengx.template.pojo.page.WhereOperator;
import com.fengx.template.task.AsyncTask;
import com.fengx.template.utils.jpa.DynamicSpecUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.io.UnsupportedEncodingException;

/**
 * test方法加事物可以不真的插入
 */
@Slf4j
@SpringBootTest
class ApplicationTests {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleDAO sysRoleDao;
    @Autowired
    private SysFilterRejectDao sysFilterRejectDao;
    @Autowired
    private SysPermissionDao sysPermissionDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysSourceDao sysSourceDao;
    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private TestDao testDao;

    @Test
    @Transactional
    void contextLoads() {
        log.info(testDao.findAll(DynamicSpecUtils.bySearchFilter(
                SearchFilter.builder()
                        .name("userTypeEnum")
                        .value("student")
                        .whereType(WhereOperator.AND)
                        .type(Operator.EQ)
                        .build())).toString());
    }

    @Test
    public void dataInit() throws UnsupportedEncodingException {
        //不过滤url
        SysFilterReject sysFilterReject = new SysFilterReject();
        sysFilterReject.setUrl("/sys/authorizing/login");
        sysFilterReject.setDesc("登录接口");
        sysFilterReject.setStatus(1);
        sysFilterRejectDao.save(sysFilterReject);
        SysFilterReject sysFilterReject1 = new SysFilterReject();
        sysFilterReject1.setUrl("/druid/**");
        sysFilterReject1.setDesc("druid数据库连接池监控页面");
        sysFilterReject1.setStatus(1);
        sysFilterRejectDao.save(sysFilterReject1);
        SysFilterReject sysFilterReject3 = new SysFilterReject();
        sysFilterReject3.setUrl("/sys/authorizing/patchca*");
        sysFilterReject3.setDesc("获取验证码接口");
        sysFilterReject3.setStatus(1);
        sysFilterRejectDao.save(sysFilterReject3);
        SysFilterReject sysFilterReject4 = new SysFilterReject();
        sysFilterReject4.setUrl("/sys/authorizing/loginNoCode");
        sysFilterReject4.setDesc("无验证码登录接口");
        sysFilterReject4.setStatus(1);
        sysFilterRejectDao.save(sysFilterReject4);
        SysFilterReject sysFilterReject5 = new SysFilterReject();
        sysFilterReject5.setUrl("/provider/source/**");
        sysFilterReject5.setDesc("公共资源访问路径");
        sysFilterReject5.setStatus(1);
        sysFilterRejectDao.save(sysFilterReject5);

        //初始化角色
        SysRole sysRole = new SysRole();
        sysRole.setName("管理员");
        sysRole.setDesc("管理员");
        sysRoleDao.save(sysRole);

        SysRole sysRole2 = new SysRole();
        sysRole2.setName("USER");
        sysRole2.setDesc("学生");
        sysRoleDao.save(sysRole2);

        //初始化权限
        SysPermission permission1 = new SysPermission();
        permission1.setUrl("/test/user");
        permission1.setDesc("测试学生权限的url");
        permission1.setRoles(Lists.newArrayList(sysRole2, sysRole));
        sysPermissionDao.save(permission1);

        SysPermission permission2 = new SysPermission();
        permission2.setUrl("/test/admin");
        permission2.setDesc("测试管理员权限的url");
        permission2.setRoles(Lists.newArrayList(sysRole));
        sysPermissionDao.save(permission2);

        //初始化用户
        SysSource source = new SysSource();
        source.setFilePath("/img/admin.png");
        source.setFileName("管理员头像");
        SysSource save = sysSourceDao.save(source);

        SysUser sysUser = new SysUser();
        sysUser.setName("白璇");
        sysUser.setUsername("admin");
        sysUser.setPassword(passwordEncoder.encode("123123"));
        sysUser.setRoles(Lists.newArrayList(sysRole));
        sysUser.setPictureId(save.getId());
        sysUser.setUserType(UserTypeEnum.ADMIN.getCode());
        sysUserDao.save(sysUser);
    }

}
