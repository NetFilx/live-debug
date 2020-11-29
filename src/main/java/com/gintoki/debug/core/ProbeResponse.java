package com.gintoki.debug.core;

import java.io.Serializable;

/**
 * @author wyh 吴永涵
 * @date 2020/3/31 4:57 下午
 * @description
 */
public class ProbeResponse<T> implements Serializable {
    private static final long serialVersionUID = 14977812506708359L;

    private T data;

    private Boolean success;

    private String msg;

    public ProbeResponse() {
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

    public static <T> ProbeResponse<T> success(T data) {
        ProbeResponse<T> r = new ProbeResponse<>();
        r.setData(data);
        r.setSuccess(Boolean.TRUE);
        return r;
    }

    public static <T> ProbeResponse<T> fail(String msg) {
        ProbeResponse<T> r = new ProbeResponse<>();
        r.setSuccess(Boolean.FALSE);
        r.setMsg(msg);
        return r;
    }
}
