package com.atguigu.gmall.common.web;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppRecordQuery extends QueryBase {

    private String id;
    private String appId;
    private String name;
    private String version;
    private String build;
    private String remark;
    private String size;
    private String fileName;
    private String operator;
    private Date createTime;
    private String modify;


    @Override
    public List<Criterion> getCriterion() {
        List<Criterion> criterion = new ArrayList<>();

        if (StringUtils.isNotEmpty(this.search)) {
            if (StringUtils.isNotEmpty(this.appId)) {
                Criterion c = new Criterion("appId = ", this.appId);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.name)) {
                Criterion c = new Criterion("name = ", this.name);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.version)) {
                Criterion c = new Criterion("version = ", this.version);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.build)) {
                Criterion c = new Criterion("build = ", this.build);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.size)) {
                Criterion c = new Criterion("size = ", this.size);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.operator)) {
                Criterion c = new Criterion("operator = ", this.operator);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.fileName)) {
                Criterion c = new Criterion("fileName = ", this.fileName);
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
        this.setProperty("createTime");
        this.setDirection(Order.Direction.DESC);
        return super.getPB();
    }


}
