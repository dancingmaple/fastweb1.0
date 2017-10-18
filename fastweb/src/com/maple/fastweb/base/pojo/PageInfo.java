package com.maple.fastweb.base.pojo;

import com.maple.fastweb.base.po.Table;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */
public class PageInfo<T extends Table> extends BaseEntity{
    private int start;
    private int num;
    private String option;  //where 后
    private String order;  //order by 后
    private int total;
    private List<T> dataList;

    public PageInfo() {
    }

    public PageInfo(int start, int num, String option, String order, int total, List<T> dataList) {
        this.start = start;
        this.num = num;
        this.option = option;
        this.order = order;
        this.total = total;
        this.dataList = dataList;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
