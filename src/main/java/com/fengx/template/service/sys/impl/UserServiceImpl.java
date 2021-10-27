package com.fengx.template.service.sys.impl;

import com.fengx.template.dao.sys.UserDAO;
import com.fengx.template.exception.WarnException;
import com.fengx.template.pojo.entity.sys.User;
import com.fengx.template.pojo.param.sys.LoginParam;
import com.fengx.template.pojo.param.sys.PwdParam;
import com.fengx.template.pojo.vo.sys.LoginVO;
import com.fengx.template.response.Response;
import com.fengx.template.service.sys.UserService;
import com.fengx.template.utils.common.JSONUtils;
import com.fengx.template.utils.common.ObjectUtils;
import com.fengx.template.utils.common.RedisUtils;
import com.fengx.template.utils.TokenUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final @NonNull RedisUtils redisUtils;
    private final @NonNull AuthenticationManager authenticationManager;
    private final @NonNull UserDAO userDAO;
    private final @NonNull PasswordEncoder passwordEncoder;

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
        String object = redisUtils.get(param.getUsername());
        if (object != null) {
            User user = JSONUtils.json2Object(object, User.class);
            String token = TokenUtils.createToken(user.getUsername());
            LoginVO copy = ObjectUtils.copy(user, new LoginVO());
            copy.setToken(token);
            return Response.success(copy);
        }
        return Response.failed("登录异常！");
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
        User user = userDAO.findFirstByUsernameAndPassword(
                pwdParam.getUsername(),
                passwordEncoder.encode(pwdParam.getPassword()));
        if (user == null) {
            throw new WarnException("账号或密码错误");
        }
        // 修改保存
        user.setPassword(passwordEncoder.encode(pwdParam.getNewPassword()));
        userDAO.save(user);
        return Response.success();
    }
}
