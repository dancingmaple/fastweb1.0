package com.maple.fastweb.test.code;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.maple.fastweb.test.code.Demo;

public interface DemoDao {

    int insert(@Param("pojo") Demo pojo);

    int insertList(@Param("pojos") List< Demo> pojo);

    List<Demo> select(@Param("pojo") Demo pojo);

    int update(@Param("pojo") Demo pojo);

}
