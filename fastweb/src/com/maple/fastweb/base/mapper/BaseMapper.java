package com.maple.fastweb.base.mapper;

import com.maple.fastweb.base.po.Table;
import com.maple.fastweb.base.pojo.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZSJ on 2017/9/27.
 * mapper  接口超类，封装常用的一些增删改查方法
 *
 * @param <K> the type parameter
 * @param <O> the type parameter
 */
@Repository
public interface BaseMapper<K,O extends Table> {

    /**
     * Delete by primary key int.
     *
     * @param k the k
     * @return the int
     */
    int deleteByPrimaryKey(K k);


    /**
     * Insert int.
     *
     * @param o the o
     * @return the int
     */
    int insert(O o);


    /**
     * Insert selective int.
     *
     * @param o the o
     * @return the int
     */
    int insertSelective(O o);


    /**
     * Select by primary key o.
     *
     * @param k the k
     * @return the o
     */
    O selectByPrimaryKey(K k);


    /**
     * Update by primary key selective int.
     *
     * @param o the o
     * @return the int
     */
    int updateByPrimaryKeySelective(O o);


    /**
     * Update by primary key int.
     *
     * @param o the o
     * @return the int
     */
    int updateByPrimaryKey(O o);

    /**
     * Count int.
     *
     * @param option the option
     * @return the int
     */
    int count(@Param("option") String option);

    /**
     * Select by condition list.
     *
     * @param pageInfo the page info
     * @return the list
     */
    List<O> selectByCondition(PageInfo<O> pageInfo);
}
