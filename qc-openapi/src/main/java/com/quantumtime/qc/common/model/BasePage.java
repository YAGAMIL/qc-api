package com.quantumtime.qc.common.model;

import java.util.List;


public class BasePage<T, M> {

    private M id;

    private Integer size;

    private Integer pageElementNum;

    private T filters;

    private List<T> data;

    public BasePage(){};

    public BasePage(M id, int size, Integer pageElementNum, List<T> data){
        this.id = id;
        this.size = size;
        this.pageElementNum = pageElementNum;
        this.data = data;
    }

    public M getId() {
        return id;
    }

    public void setId(M id) {
        this.id = id;
    }

    public Integer getSize() {
        if(size == null){
            return 10;
        }else if(size > 20){
            return 20;
        }else {
            return size;
        }
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPageElementNum() {
        return pageElementNum;
    }

    public void setPageElementNum(Integer pageElementNum) {
        this.pageElementNum = pageElementNum;
    }

    public T getFilters() {
        return filters;
    }

    public void setFilters(T filters) {
        this.filters = filters;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
