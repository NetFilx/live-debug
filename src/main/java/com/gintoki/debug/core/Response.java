package com.gintoki.debug.core;

import java.io.Serializable;

/**
 * @author  wyh 吴永涵
 * @date  2020/3/31 4:57 下午
 * @description  
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 14977812506708359L;

    private T data;

    private Boolean success;

    private String msg;

    public Response() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static <T> Response<T> success(T data) {
        Response<T> r = new Response<>();
        r.setData(data);
        r.setSuccess(Boolean.TRUE);
        return r;
    }

    public static <T> Response<T> fail(String msg) {
        Response<T> r = new Response<>();
        r.setSuccess(Boolean.FALSE);
        r.setMsg(msg);
        return r;
    }
}
