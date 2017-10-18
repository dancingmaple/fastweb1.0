package com.maple.fastweb.test.code;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.maple.fastweb.test.code.Demo;
import com.maple.fastweb.test.code.DemoDao;

//@Service
public class DemoService {

    //@Resource
    private DemoDao demoDao;

    public int insert(Demo pojo){
        return demoDao.insert(pojo);
    }

    public int insertList(List< Demo> pojos){
        return demoDao.insertList(pojos);
    }

    public List<Demo> select(Demo pojo){
        return demoDao.select(pojo);
    }

    public int update(Demo pojo){
        return demoDao.update(pojo);
    }

}
