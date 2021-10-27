package com.fengx.template.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "响应数据主体")
@Data
// 返回字段为空 则隐藏
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody<T> {

    @ApiModelProperty(value = "状态码")
    private int status;

    @ApiModelProperty(value = "响应信息")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    public ResponseBody(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseBody(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseBody(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ResponseBody(ResponseEnum response) {
        this.status = response.getCode();
        this.message = response.getMessage();
    }

    public ResponseBody(ResponseEnum response, T data) {
        this.status = response.getCode();
        this.message = response.getMessage();
        this.data = data;
    }
}
