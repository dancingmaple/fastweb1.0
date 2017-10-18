package com.maple.fastweb.base.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/26.
 */
public class BaseEntity implements Serializable{
    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public String toString() {
        return this.toJson();
    }
}
