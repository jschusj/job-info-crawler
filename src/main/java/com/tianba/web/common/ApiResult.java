package com.tianba.web.common;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ApiResult<T> {
    private static final long serialVersionUID = -2254621671109894906L;
    private int code = 200;
    private boolean success;
    private String message;
    private T data;

    public static ApiResult success() {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(true);
        result.setMessage("操作成功");
        result.setData(null);
        return result;
    }

    public static ApiResult fail() {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(false);
        result.setMessage("操作失败");
        result.setData(null);
        result.setCode(400);
        return result;
    }

    public static ApiResult fail(String message) {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        result.setData(null);
        result.setCode(400);
        return result;
    }

    public static ApiResult fail(int code, String message) {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        result.setData(null);
        result.setCode(code);
        return result;
    }

    public static ApiResult success(Object data) {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(true);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static ApiResult success(Object data, String message) {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static ApiResult success(String message) {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static ApiResult success(int code, String message) {
        ApiResult<Object> result = new ApiResult<>();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(null);
        result.setCode(code);
        return result;
    }

    public ApiResult() {

    }

    public ApiResult(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ApiResult(T data, String message, boolean success, int code) {
        this.data = data;
        this.message = message;
        this.success = success;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                ToStringStyle.SIMPLE_STYLE);
    }
}
