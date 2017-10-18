package com.maple.fastweb.base.pojo;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2017/9/26.
 */
public class Log extends BaseEntity {
    static Logger logger = Logger.getLogger("fast web log");

    public static void d(String msg) {
        logger.debug(msg);
    }

    public static void e(String msg) {
        logger.error(msg);
    }

    public static void i(String msg) {
        logger.info(msg);
    }

}
