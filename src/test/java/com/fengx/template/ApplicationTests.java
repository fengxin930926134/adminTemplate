package com.fengx.template;

import com.fengx.template.dao.sys.RoleDAO;
import com.fengx.template.dao.sys.SourceDAO;
import com.fengx.template.dao.sys.UserDAO;
import com.fengx.template.pojo.entity.sys.Permission;
import com.fengx.template.pojo.entity.sys.Role;
import com.fengx.template.pojo.entity.sys.Source;
import com.fengx.template.pojo.entity.sys.User;
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
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDao;
    @Autowired
    private SysFilterRejectDao sysFilterRejectDao;
    @Autowired
    private SysPermissionDao sysPermissionDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SourceDAO sourceDAO;
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
        Role role = new Role();
        role.setName("管理员");
        role.setDesc("管理员");
        roleDao.save(role);

        Role role2 = new Role();
        role2.setName("USER");
        role2.setDesc("学生");
        roleDao.save(role2);

        //初始化权限
        Permission permission1 = new Permission();
        permission1.setUrl("/test/user");
        permission1.setDesc("测试学生权限的url");
        permission1.setRoles(Lists.newArrayList(role2, role));
        sysPermissionDao.save(permission1);

        Permission permission2 = new Permission();
        permission2.setUrl("/test/admin");
        permission2.setDesc("测试管理员权限的url");
        permission2.setRoles(Lists.newArrayList(role));
        sysPermissionDao.save(permission2);

        //初始化用户
        Source source = new Source();
        source.setFilePath("/img/admin.png");
        source.setFileName("管理员头像");
        Source save = sourceDAO.save(source);

        User user = new User();
        user.setName("白璇");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("123123"));
        user.setRoles(Lists.newArrayList(role));
        user.setPictureId(save.getId());
        user.setUserType(UserTypeEnum.ADMIN.getCode());
        userDAO.save(user);
    }

}
