package com.fengx.template.response;

import io.swagger.annotations.ApiModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@ApiModel(value = "响应体")
public class Response<T> extends ResponseEntity<T> {

    public Response(T body) {
        super(body, HttpStatus.OK);
    }

    public Response(T body, HttpStatus status) {
        super(body, status);
    }

    public Response(T body, MultiValueMap<String, String> headers) {
        super(body, headers, HttpStatus.OK);
    }

    // --------------------------------------实例化部分--------------------------------------

    /**
     * 警告
     */
    public static <T> Response<ResponseBody<T>> warn(String message) {
        return new Response<>(new ResponseBody<>(ResponseEnum.WARN.getCode(), message));
    }

    public static <T> Response<ResponseBody<T>> success() {
        return new Response<>(new ResponseBody<>(ResponseEnum.SUCCESS));
    }

    public static <T> Response<ResponseBody<T>> success(T data) {
        return new Response<>(new ResponseBody<>(ResponseEnum.SUCCESS, data));
    }

    public static <T> Response<ResponseBody<T>> failed() {
        return new Response<>(new ResponseBody<>(ResponseEnum.FAIL));
    }

    /**
     * 操作失败 但是还是成功请求了
     */
    public static <T> Response<ResponseBody<T>> failed(String message) {
        return new Response<>(new ResponseBody<>(ResponseEnum.FAIL.getCode(), message));
    }

    public static <T> Response<ResponseBody<T>> notLogin() {
        return new Response<>(new ResponseBody<>(ResponseEnum.NOT_LOGIN), HttpStatus.UNAUTHORIZED);
    }

    public static <T> Response<ResponseBody<T>> foundNot() {
        return new Response<>(new ResponseBody<>(ResponseEnum.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    public static <T> Response<ResponseBody<T>> notPermission() {
        return new Response<>(new ResponseBody<>(ResponseEnum.NOT_PERMISSION), HttpStatus.FORBIDDEN);
    }

    public static <T> Response<ResponseBody<T>> tokenInvalid() {
        return new Response<>(new ResponseBody<>(ResponseEnum.TOKEN_INVALID));
    }
}
