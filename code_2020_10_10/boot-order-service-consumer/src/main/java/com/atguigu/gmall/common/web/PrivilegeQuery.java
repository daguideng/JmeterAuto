package com.atguigu.gmall.common.web;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrivilegeQuery extends QueryBase {

    private Integer id;
    private String code;
    private String description;
    private String pid;
    private String pageUrl;
    private String type;
    private String enable;
    private String parentCode;
    private Date createTime;


    @Override
    public List<Criterion> getCriterion() {
        List<Criterion> criterion = new ArrayList<>();

        if (StringUtils.isNotEmpty(this.search)) {
            if (StringUtils.isNotEmpty(this.code)) {
                Criterion c = new Criterion("code = ", this.code);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.type)) {
                Criterion c = new Criterion("type = ", this.type);
                criterion.add(c);
            }
            if (this.enable != null) {
                Criterion c = new Criterion("enable = ", this.enable);
                criterion.add(c);
            }
        }

        return criterion.isEmpty() ? null : criterion;
    }


    /*
         * (non-Javadoc)
         * @see com.ucredit.wit.common.page.QueryBase#getPB()
         */
    @Override
    public PageBounds getPB() {
        this.setProperty("id");
        this.setDirection(Order.Direction.ASC);
        return super.getPB();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PrivilegeQuery{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", pid='" + pid + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", type='" + type + '\'' +
                ", enable='" + enable + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
