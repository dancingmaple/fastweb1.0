package com.maple.fastweb.service;

import com.maple.fastweb.base.service.BaseService;
import com.maple.fastweb.mybatis.model.Commonlog;

/**
 * Created by Administrator on 2017/9/29.
 */
public interface LogService extends BaseService <Long,Commonlog>{
    /**
     * Add log int.
     *
     * @param log the log
     * @return the int
     */
    int addLog(Commonlog log);

}
