package com.maple.fastweb.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maple.fastweb.base.pojo.Log;
import com.maple.fastweb.base.pojo.Result;
import com.xiaoleilu.hutool.http.HtmlUtil;
import okhttp3.*;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

public class OkHttpUtil {
    public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
    public static final String BASEURL = "http://localhost:8180/fastweb";
    public static final String token = "123456789";

   static OkHttpClient client = new OkHttpClient();

   public static String post(String url, String json) throws IOException {
	  System.out.println("json = "+json);
	  JSONObject object = JSONObject.parseObject(json);
    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();
    try {
    	Response response = client.newCall(request).execute();
    	return response.body().string();
    	} catch (Exception e) {
    		return "fail";
		}
      
  }

    public static void main(String[] args) throws IOException {

    String re = OkHttpUtil.post(BASEURL+"/user/test","");
        Log.i(re);
        Result result = com.alibaba.fastjson.JSON.parseObject(re,Result.class);
        Log.e(result.getErr());

  }
}