package com.mall.common.api;

import com.github.pagehelper.PageInfo;

import java.util.List;

public class ResponsePage <T>{
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 分页数据
     */
    private List<T> list;


    public static <T> ResponsePage<T> getPage(List<T> list){
        ResponsePage<T> responsePage = new ResponsePage<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        responsePage.setPageNum(pageInfo.getPageNum());
        responsePage.setPageSize(pageInfo.getPageSize());
        responsePage.setTotalPage(pageInfo.getPages());
        responsePage.setTotal(pageInfo.getTotal());
        responsePage.setList(pageInfo.getList());
        return responsePage;
    }

    public static <T> ResponsePage<T> getPage(Integer pageNum , List<T> list){
        ResponsePage<T> responsePage = new ResponsePage<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        responsePage.setPageNum(pageNum);
        responsePage.setPageSize(pageInfo.getPageSize());
        responsePage.setTotalPage(pageInfo.getPages());
        responsePage.setTotal(pageInfo.getTotal());
        responsePage.setList(pageInfo.getList());
        return responsePage;
    }

    public static <T> ResponsePage<T> getPage(PageInfo<T> pageInfo){
        ResponsePage<T> page = new ResponsePage<>();
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalPage(pageInfo.getPages());
        page.setList(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    public static <T> ResponsePage<T> getPage(Integer pageNum,PageInfo<T> pageInfo){
        ResponsePage<T> page = new ResponsePage<>();
        page.setPageNum(pageNum);
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalPage(pageInfo.getPages());
        page.setList(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }


    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Long getTotal() {
        return total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setPageNum(Integer pageNum) {
        this.pageSize = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}