package com.maple.fastweb.base.service;

import com.maple.fastweb.base.mapper.BaseMapper;
import com.maple.fastweb.base.po.Table;
import com.maple.fastweb.base.pojo.PageInfo;

import java.util.List;

/**
 * Created by ZSJ on 2017/9/27.
 * service 的 基类
 *
 * @param <K> the type parameter
 * @param <O> the type parameter
 */
public interface BaseService<K,O extends Table>  {
    /**
     * Find one o.
     *
     * @param k the k
     * @return the o
     */
    O findOne(K k);

    /**
     * Find list list.
     *
     * @param where the where
     * @param order the order
     * @return the list
     */
    List<O> findList(String where ,String order);

    /**
     * Find by page page info.
     *
     * @param pageInfo the page info
     * @return the page info
     */
    PageInfo<O> findByPage(PageInfo<O> pageInfo);

    /**
     * Delete one o.
     *
     * @param k the k
     * @return the o
     */
    O deleteOne(K k);

    /**
     * Update one o.
     *
     * @param o the o
     * @return the o
     */
    O updateOne(O o);

    /**
     * Add one o.
     *
     * @param o the o
     * @return the o
     */
    O addOne(O o);

    /**
     * Count int.
     *
     * @param condition the condition
     * @return the int
     */
    int count(String condition);
}
