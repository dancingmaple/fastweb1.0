package com.maple.fastweb.service.impl;

import com.maple.fastweb.base.mapper.BaseMapper;
import com.maple.fastweb.base.pojo.PageInfo;
import com.maple.fastweb.base.service.BaseServiceImpl;
import com.maple.fastweb.mybatis.mapper.UserMapper;
import com.maple.fastweb.mybatis.model.User;
import com.maple.fastweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/9/27.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<Long,User> implements UserService {
    public UserServiceImpl(UserMapper baseMapper) {
        super(baseMapper);
    }

    @Override
    @Cacheable(value="setting",key=" #pageInfo.start + #pageInfo.num " )
    public PageInfo<User> findByPage(PageInfo<User> pageInfo) {
        return super.findByPage(pageInfo);
    }
}
