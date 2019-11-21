package com.alibaba.bean;

/**
 * 向前端返回信息封装
 * @param <T> 可变类型
 */
public class Result<T> {

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
//ashduhasoduhsaod
    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}