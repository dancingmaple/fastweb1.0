package com.maple.fastweb.base.po;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by zsj on 2017/9/26.
 * base persistent object reflects a mysql table
 */
public class Table implements Serializable {
    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public String toString() {
        return this.toJson();
    }
}
