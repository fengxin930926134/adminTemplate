package com.fengx.template;

import com.fengx.template.dao.sys.*;
import com.fengx.template.pojo.entity.sys.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDao;
    @Autowired
    private PermissionRoleDAO permissionRoleDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PermissionDAO permissionDAO;

    /**
     * test方法加事物可以不真的插入
     */
    @Test
    @Transactional
    void contextLoads() {
//        log.info(testDao.findAll(DynamicSpecUtils.bySearchFilter(
//                SearchFilter.builder()
//                        .name("userTypeEnum")
//                        .value("student")
//                        .whereType(WhereOperator.AND)
//                        .type(Operator.EQ)
//                        .build())).toString());
    }

    /**
     * 用作初始化数据
     */
    @Test
    public void dataInit() {
        //不过滤url
        Permission sysFilterReject = new Permission();
        sysFilterReject.setUrl("/sys/authorizing/*");
        sysFilterReject.setRemarks("放开登录相关接口");
        sysFilterReject.setStatus(1);
        sysFilterReject.setType(1);
        permissionDAO.save(sysFilterReject);
        Permission sysFilterReject1 = new Permission();
        sysFilterReject1.setUrl("/druid/**");
        sysFilterReject1.setRemarks("druid数据库连接池监控页面");
        sysFilterReject1.setStatus(1);
        sysFilterReject1.setType(1);
        permissionDAO.save(sysFilterReject1);
        Permission sysFilterReject5 = new Permission();
        sysFilterReject5.setUrl("/provider/source/**");
        sysFilterReject5.setRemarks("公共资源访问路径");
        sysFilterReject5.setStatus(1);
        sysFilterReject5.setType(1);
        permissionDAO.save(sysFilterReject5);
        Permission sysFilterReject6 = new Permission();
        sysFilterReject6.setUrl("/provider/source/**");
        sysFilterReject6.setRemarks("公共资源访问路径");
        sysFilterReject6.setStatus(1);
        sysFilterReject6.setType(1);
        permissionDAO.save(sysFilterReject6);

        //初始化角色
        Role role = new Role();
        role.setName("超管员");
        role.setRemarks("超管员");
        Role save = roleDao.saveAndFlush(role);

        Role role2 = new Role();
        role2.setName("普通用户");
        role2.setRemarks("普通用户");
        Role save1 = roleDao.saveAndFlush(role2);

        //初始化权限
        Permission permission1 = new Permission();
        permission1.setUrl("/test/user");
        permission1.setRemarks("测试普通用户权限的url");
        permission1.setType(2);
        permission1.setStatus(1);
        Permission save2 = permissionDAO.saveAndFlush(permission1);

        Permission permission2 = new Permission();
        permission2.setUrl("/test/admin");
        permission2.setRemarks("测试超管员权限的url");
        permission2.setStatus(1);
        Permission save3 = permissionDAO.saveAndFlush(permission2);

        // 关联权限
        PermissionRole permissionRole = new PermissionRole();
        permissionRole.setRoleId(save.getId());
        permissionRole.setPermissionId(save3.getId());
        permissionRoleDAO.save(permissionRole);
        permissionRole.setRoleId(save.getId());
        permissionRole.setPermissionId(save2.getId());
        permissionRoleDAO.save(permissionRole);

        PermissionRole permissionRole1 = new PermissionRole();
        permissionRole1.setRoleId(save1.getId());
        permissionRole1.setPermissionId(save2.getId());
        permissionRoleDAO.save(permissionRole1);

        User user = new User();
        user.setName("风");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("123123"));
        userDAO.save(user);
    }

}
