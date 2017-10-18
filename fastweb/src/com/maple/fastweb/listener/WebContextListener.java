package com.maple.fastweb.listener;



import com.maple.fastweb.util.ThreadUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Administrator on 2017/9/15.
 */
public class WebContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("WebContextListener contextInitialized server started ");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("WebContextListener contextDestroyed server closed ");
        try {
            ThreadUtil.shutDownExecutors();
        } catch (InterruptedException e) {
            System.out.println("WebContextListener contextDestroyed Excutors shutdown failed ");
        }
    }
}
