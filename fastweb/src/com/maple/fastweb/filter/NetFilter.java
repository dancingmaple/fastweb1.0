package com.maple.fastweb.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.maple.fastweb.base.pojo.Result;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;


public class NetFilter implements Filter {

    RateLimiter rateLimiter = RateLimiter.create(1000);


    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
            /*bits +=request.toString().getBytes().length;
			System.out.println(request.getContentLength());
			System.out.println(request.toString().getBytes().length);
			System.out.println(bits);*/
      //  HttpServletRequest httpRequest = null;

        WrappedHttpServletRequest requestWrapper = new WrappedHttpServletRequest((HttpServletRequest) request);
       /* if (request instanceof HttpServletRequest) {
            httpRequest = (HttpServletRequest) request;
        }*/
       if (requestWrapper.getRequestURI().contains("order/")){
           filterChain.doFilter(request,response);
       }
        String param = requestWrapper.getRequestParams();
        System.out.println("NetFilter读取body中的参数>>>>>>>>>" + param);
        /*if ("POST".equalsIgnoreCase(requestWrapper.getMethod().toUpperCase())) {
            param = requestWrapper.getRequestParams();
            System.out.println("NetFilter读取body中的参数>>>>>>>>>" + param);
        }*/
        // 限流控制，每秒1000次
        if (!rateLimiter.tryAcquire()) {
            System.out.println("too many requests,abandon");
            Result result = new Result();
            result.setErr("too many requests,abandon");
            response.getWriter().write(result.toJson());
            response.getWriter().flush();
            return;
        }
        String uri = requestWrapper.getRequestURI();
        ThreadServletCache.setPostRequestParams(uri+"_p_"+param);   //存储到ThreadLocal中，方便随时获取
        filterChain.doFilter(requestWrapper, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;
    }

}
