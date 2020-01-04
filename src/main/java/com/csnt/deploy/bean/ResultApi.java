package com.csnt.deploy.bean;

import java.io.Serializable;

/**
 * @ClassName ResultApi
 * @Description TODO
 * @Author duwanjiang
 * @Date 2020/1/2 20:32
 * Version 1.0
 **/
public class ResultApi<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public ResultApi() {
        this.code = 200;
    }

    public ResultApi(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultApi{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
