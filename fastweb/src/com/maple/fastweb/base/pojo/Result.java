package com.maple.fastweb.base.pojo;

/**
 * Created by Administrator on 2017/9/26.
 */
public class Result extends BaseEntity {
    private int code = 0; // 操作成功
    private Object result = new Object();
    private String err = "";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

}
