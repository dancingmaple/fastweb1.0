package com.maple.fastweb.service.impl;

import com.maple.fastweb.base.mapper.BaseMapper;
import com.maple.fastweb.base.service.BaseServiceImpl;
import com.maple.fastweb.mybatis.mapper.CommonlogMapper;
import com.maple.fastweb.mybatis.model.Commonlog;
import com.maple.fastweb.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/9/29.
 */
@Service
public class LogServiceImpl extends BaseServiceImpl<Long,Commonlog> implements LogService{

    public LogServiceImpl(CommonlogMapper baseMapper) {
        super(baseMapper);
    }

    @Override
    public int addLog(Commonlog log) {
        return baseMapper.insertSelective(log);
    }



    /*@Override
    public int addLog(Commonlog log) {
        return baseMapper.insertSelective(log);
    }*/
}
