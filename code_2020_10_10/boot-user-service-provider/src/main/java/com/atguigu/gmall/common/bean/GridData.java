package com.atguigu.gmall.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @ClassName: DataGridResult
 * @Description: 封装前台返回表格数据
 * @author shirui
 * @date 2018年4月10日
 *
 */
public class GridData<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private long totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;
    private List<T> list;

    public GridData(List<T> list, long totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


}