package com.fengx.template.exception.handler;

import com.fengx.template.exception.*;
import com.fengx.template.response.Response;
import io.lettuce.core.RedisConnectionException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.List;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获警告
     */
    @ExceptionHandler(WarnException.class)
    public Response<?> warnException(WarnException e) {
        e.printStackTrace();
        return Response.warn(e.getMessage());
    }

    /**
     * 未使用转换实体注解
     */
    @ExceptionHandler(BindException.class)
    public Response<?> bindException() {
        log.error("未使用注解 @RequestBody 转换实体导致实体转换失败");
        return Response.failed("转换实体失败");
    }

    /**
     * 参数合法性校验异常
     * p:通常是参数和实体匹配不上导致json转换失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<?> validationException(HttpMessageNotReadableException e) {
        e.printStackTrace();
        return Response.warn("参数不合法");
    }

    /**
     * 参数合法性校验异常
     * p:有参校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> validationException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error: errors) {
                return Response.warn(error.getDefaultMessage());
            }
        }
        return Response.warn("参数异常");
    }

    /**
     * 验证码异常
     */
    @ExceptionHandler(PatchaException.class)
    public Response<?> patchaExceptionHandler(PatchaException e) {
        return Response.warn(e.getMessage());
    }

    /**
     * 文件上传异常
     */
    @ExceptionHandler(MultipartException.class)
    public Response<?> multipartExceptionHandler(MultipartException e) {
        return Response.failed(e.getCause().getMessage());
    }

    /**
     * 找不到资源异常
     */
    @ExceptionHandler({NoHandlerFoundException.class, NotFoundException.class})
    public Response<?> notFoundExceptionHandler() {
        return Response.foundNot();
    }

    /**
     * 无访问权限异常
     */
    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class, PermissionException.class})
    public Response<?> permissionExceptionHandler() {
        return Response.notPermission();
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Response<?> nullPointerExceptionHandler(NullPointerException e) {
        e.printStackTrace();
        return Response.failed("空指针异常");
    }

    /**
     * token异常
     */
    @ExceptionHandler(TokenException.class)
    public Response<?> tokenExceptionHandler() {
        return Response.tokenInvalid();
    }

    /**
     * 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public Response<?> notLoginExceptionHandler() {
        return Response.notLogin();
    }

    /**
     * 身份验证失败
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public Response<?> authenticationExceptionHandler() {
        return Response.failed("账号或密码不正确");
    }

    /**
     * 未开启redis服务
     */
    @ExceptionHandler({RedisConnectionException.class, RedisConnectionFailureException.class})
    public Response<?> redisConnectionExceptionHandler() {
        return Response.warn("未开启redis服务");
    }

    /**
     * 其它异常
     */
    @ExceptionHandler(Exception.class)
    public Response<?> exceptionHandler(Exception e) {
        e.printStackTrace();
        return Response.warn(e.getMessage() != null? e.getMessage(): "发生异常");
    }

}