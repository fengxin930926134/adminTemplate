package com.fengx.template.service.sys.impl;

import com.fengx.template.exception.WarnException;
import com.fengx.template.pojo.enums.UserTypeEnum;
import com.fengx.template.pojo.excel.ExcelTableHead;
import com.fengx.template.pojo.page.PageData;
import com.fengx.template.pojo.page.PageParam;
import com.fengx.template.pojo.param.IdsParam;
import com.fengx.template.pojo.param.LoginParam;
import com.fengx.template.pojo.param.PwdParam;
import com.fengx.template.pojo.param.UserAddParam;
import com.fengx.template.pojo.vo.LoginVo;
import com.fengx.template.pojo.vo.UserVo;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.SysUserService;
import com.fengx.template.utils.common.DateUtils;
import com.fengx.template.utils.common.ObjectUtils;
import com.fengx.template.dao.*;
import com.fengx.template.pojo.entity.*;
import com.fengx.template.utils.ExcelUtils;
import com.fengx.template.utils.RedisUtils;
import com.fengx.template.utils.TokenUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final @NonNull RedisUtils<SysUser> redisUtils;
    private final @NonNull AuthenticationManager authenticationManager;
    private final @NonNull SysUserDao sysUserDao;
    private final @NonNull SysSourceDao sysSourceDao;
    private final @NonNull SysUserInfoDao sysUserInfoDao;
    private final @NonNull PasswordEncoder passwordEncoder;
    private final @NonNull SysRoleDAO sysRoleDao;
    private final @NonNull CostTypeDao costTypeDao;
    private final @NonNull PayDetailsDao payDetailsDao;
    private final @NonNull RegProcessUserLinkDao regProcessUserLinkDao;
    private final @NonNull RegProcessDao regProcessDao;
    private final @NonNull DormitoryUserLinkDao dormitoryUserLinkDao;

    @Override
    public Response<?> login(LoginParam param) {
        // 1.创建Authentication 获取user信息
        UsernamePasswordAuthenticationToken request
                = new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword());
        // 2.认证 （验证密码之类）
        Authentication result = authenticationManager.authenticate(request);
        // 3.保存认证信息
        SecurityContextHolder.getContext().setAuthentication(result);
        // 4.组装返回信息
        SysUser sysUser = redisUtils.get(param.getUsername());
        String token = TokenUtils.createToken(sysUser.getUsername());
        LoginVo copy = ObjectUtils.copy(sysUser, new LoginVo());
        copy.setToken(token);
        return Response.success(copy);
    }

    @Override
    public Response<?> page(PageParam param) {
        // 查询
        Page<SysUser> page = sysUserDao.findAll(param);
        // 补充
        List<UserVo> userVos = supplementStuInfo(page.getContent());
        return Response.success(new PageData(userVos, page.getTotalElements(), page.getTotalPages()));
    }

    @Override
    @Transactional
    public Response<?> add(UserAddParam param) {
        if (sysUserDao.existsByUsername(param.getSno())) {
            return Response.failed("此账号已存在");
        }
        SysUser user = new SysUser();
        Map<String, SysRole> roleMap = sysRoleDao.findAll().stream().collect(Collectors.toMap(SysRole::getName, Function.identity()));
        // 补充信息表
        if (param.getUserType() == UserTypeEnum.STUDENT.getCode()) {
            SysUserInfo info = ObjectUtils.copy(param, new SysUserInfo());
            SysUserInfo userInfo = sysUserInfoDao.save(info);
            user.setUserInfoId(userInfo.getId());
            user.setRoles(Lists.newArrayList(roleMap.get("USER")));
        }
        // 用户基本表
        user.setUserType(UserTypeEnum.checkUserType(param.getUserType()));
        user.setName(param.getName());
        user.setUsername(param.getSno());
        user.setPassword(passwordEncoder.encode(param.getPassword()));
        SysUser save = sysUserDao.save(user);
        if (param.getUserType() == 1) {
            // 额外补充当前费用和进度表
            supplementMsg(Lists.newArrayList(save));
        }
        return Response.success();
    }

    @Override
    @Transactional
    public Response<?> delete(IdsParam param) {
        List<SysUser> users = sysUserDao.findAllById(param.getIds());
        if (ObjectUtils.checkValue(users)) {
            Set<String> userIds = users.stream().map(SysUser::getId).collect(Collectors.toSet());
            if (users.size() > 0) {
                // 删除付费信息
                payDetailsDao.deleteAllByUserIdIn(userIds);
                // 删除报到进度信息
                regProcessUserLinkDao.deleteAllByUserIdIn(userIds);
                // 删除宿舍分配
                dormitoryUserLinkDao.deleteAllByUserIdIn(userIds);
                // 删除学生
                sysUserDao.deleteInBatch(users);
            }
        }
        return Response.success();
    }

    @Override
    @Transactional
    public Response<?> importExcel(MultipartFile file) {
        List<Map<String, String>> data = ExcelUtils.importExcelAnalysis(file, 2);
        List<SysUser> users = Lists.newArrayList();
        if (ObjectUtils.checkValue(data)) {
            SysRole role = sysRoleDao.findByName("USER");
            // 保存
            for (Map<String, String> map: data) {
                SysUser user = new SysUser();
                SysUserInfo info = new SysUserInfo();
                user.setName(map.get("用户名"));
                info.setSex(map.get("性别").equals("男")? 1: 0);
                info.setSno(map.get("高考号"));
                user.setUsername(map.get("高考号"));
                info.setIdCard(map.get("身份证号"));
                // 默认密码
                user.setPassword(passwordEncoder.encode("123123"));
                info.setSchool(map.get("毕业学校"));
                info.setHkale(Double.parseDouble(map.get("高考成绩")));
                info.setGraduationTime(DateUtils.shortDate(map.get("毕业时间")));
                user.setUserType(UserTypeEnum.STUDENT.getCode());
                user.setRoles(Lists.newArrayList(role));
                SysUserInfo save = sysUserInfoDao.save(info);
                user.setUserInfoId(save.getId());
                users.add(user);
            }
            users = sysUserDao.saveAll(users);
            // 补充其它相关信息
            supplementMsg(users);
        }
        return Response.success();
    }

    @Override
    public Response<?> downloadExcel() {
        // 查询学生
        List<SysUser> allUser = sysUserDao.findAllByUserType(1);
        List<UserVo> userVos = supplementStuInfo(allUser);
        // 组装表头
        List<ExcelTableHead> heads = Lists.newArrayList(
            new ExcelTableHead("用户名"),
            new ExcelTableHead("性别"),
            new ExcelTableHead("高考号"),
            new ExcelTableHead("身份证号"),
            new ExcelTableHead("毕业学校"),
            new ExcelTableHead("高考成绩"),
            new ExcelTableHead("毕业时间")
        );
        // 组装数据
        List<List<String>> data = Lists.newArrayList();
        userVos.forEach(item -> data.add(Lists.newArrayList(
                item.getName() == null? "": item.getName(),
                item.getSex() == 0? "女": "男",
                item.getSno() == null? "": item.getSno(),
                item.getIdCard() == null? "": item.getIdCard(),
                item.getSchool() == null? "": item.getSchool(),
                item.getHkale() == null? "": item.getHkale().toString(),
                item.getGraduationTime() == null? "": DateUtils.shortStr(item.getGraduationTime())
        )));
        // 返回excel
        return ExcelUtils.exportExcelDownload("学生信息表", heads, data);
    }

    @Override
    public Response<?> upPassword(PwdParam pwdParam) {
        // 验证
        if (!pwdParam.getNewPassword().equals(pwdParam.getRePassword())) {
            throw new WarnException("两次输入密码不一致");
        }
        if (pwdParam.getPassword().equals(pwdParam.getNewPassword())) {
            throw new WarnException("新密码和旧密码相同");
        }
        SysUser user = sysUserDao.findFirstByUsernameAndPassword(
                pwdParam.getUsername(),
                passwordEncoder.encode(pwdParam.getPassword()));
        if (user == null) {
            throw new WarnException("账号或密码错误");
        }
        // 修改保存
        user.setPassword(passwordEncoder.encode(pwdParam.getNewPassword()));
        sysUserDao.save(user);
        return Response.success();
    }

    /**
     * 额外补充当前费用和进度表
     * @param sysUsers 用户
     */
    private void supplementMsg(List<SysUser> sysUsers) {
        Set<String> costIds = costTypeDao.findAll().stream().map(CostType::getId).collect(Collectors.toSet());
        RegProcess process = regProcessDao.findByPid("#");
        // 付费信息
        List<PayDetails> payDetails = Lists.newArrayList();
        // 报到信息
        List<RegProcessUserLink> links = Lists.newArrayList();
        sysUsers.forEach(sysUser -> {
            costIds.forEach(costId -> payDetails.add(new PayDetails(sysUser.getId(), costId, -1)));
            if (process != null) {
                links.add(new RegProcessUserLink(sysUser.getId(), process.getId(), process.getProName()));
            }
        });
        payDetailsDao.saveAll(payDetails);
        regProcessUserLinkDao.saveAll(links);
    }

    /**
     * 补充学生信息
     * @param sysUsers list
     * @return vo
     */
    private List<UserVo> supplementStuInfo(List<SysUser> sysUsers) {
        // 返回数据
        List<UserVo> userVos = Lists.newArrayList();
        if (sysUsers.size() > 0) {
            // 用户信息id
            Set<String> userInfoIds = Sets.newHashSet();
            // 头像id
            Set<String> pictureIds = Sets.newHashSet();
            sysUsers.forEach(item -> {
                pictureIds.add(item.getPictureId());
                userInfoIds.add(item.getUserInfoId());
            });
            // 查询额外数据
            Map<String, SysSource> sourceMap = sysSourceDao.findAllById(pictureIds)
                    .stream().collect(Collectors.toMap(SysSource::getId, Function.identity()));
            Map<String, SysUserInfo> userInfoMap = sysUserInfoDao.findAllById(userInfoIds)
                    .stream().collect(Collectors.toMap(SysUserInfo::getId, Function.identity()));
            // 补充数据
            sysUsers.forEach(item -> {
                UserVo userVo = ObjectUtils.copy(item, new UserVo());
                SysUserInfo userInfo = userInfoMap.get(item.getUserInfoId());
                SysSource source = item.getPictureId() != null? sourceMap.get(item.getPictureId()): null;
                if (userInfo != null) {
                    userVo = ObjectUtils.copy(userInfo, userVo);
                    userVo.setId(item.getId());
                }
                if (source != null) {
                    userVo.setFilePath(source.getFilePath());
                }
                userVos.add(userVo);
            });
        }
        return userVos;
    }
}
