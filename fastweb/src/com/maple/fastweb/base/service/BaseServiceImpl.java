package com.maple.fastweb.base.service;

import com.maple.fastweb.base.mapper.BaseMapper;
import com.maple.fastweb.base.po.Table;
import com.maple.fastweb.base.pojo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */

public abstract class BaseServiceImpl<K,O extends Table> implements BaseService<K,O>{


    public BaseMapper<K,O> baseMapper;

    @Autowired
    public BaseServiceImpl(BaseMapper<K, O> baseMapper) {
        this.baseMapper = baseMapper;
    }


    /*public void setBaseMapper(BaseMapper<K, O> baseMapper) {
        this.baseMapper = baseMapper;
    }*/

    @Override
    public O findOne(K k) {
        return baseMapper.selectByPrimaryKey(k);
    }

    @Override
    public List<O> findList(String where, String order) {
        return null;
    }

    @Override
    public PageInfo<O> findByPage(PageInfo<O> pageInfo) {
        List<O> datalist = baseMapper.selectByCondition(pageInfo);
        if (datalist == null || datalist.size() <= 0){
            datalist = new ArrayList<>();
        }
        String option = pageInfo.getOption();
        int count = baseMapper.count(option);
        pageInfo.setDataList(datalist);
        pageInfo.setTotal(count);
        return pageInfo;
    }

    @Override
    public O deleteOne(K k) {
        return null;
    }

    @Override
    public O updateOne(O o) {
        return null;
    }

    @Override
    public O addOne(O o) {
        baseMapper.insert(o);
        return o;
    }

    @Override
    public int count(String condition) {
        return baseMapper.count(condition);
    }
}
