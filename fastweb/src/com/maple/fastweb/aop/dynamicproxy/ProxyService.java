package com.maple.fastweb.aop.dynamicproxy;

/**
 * Created by Administrator on 2017/9/28.
 */
public interface ProxyService {
    /**
     * Gets name.
     *
     * @param id the id
     * @return the name
     */
    public String getName(int id);

    /**
     * Gets age.
     *
     * @param id the id
     * @return the age
     */
    public Integer getAge(int id);
}
