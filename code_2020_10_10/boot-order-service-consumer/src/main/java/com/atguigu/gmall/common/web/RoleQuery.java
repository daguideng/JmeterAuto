package com.atguigu.gmall.common.web;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RoleQuery extends QueryBase {

    private String id;
    private String code;
    private String name;
    private String enable;


    @Override
    public List<Criterion> getCriterion() {
        List<Criterion> criterion = new ArrayList<>();

        if (StringUtils.isNotEmpty(this.search)) {
            if (StringUtils.isNotEmpty(this.code)) {
                Criterion c = new Criterion("code = ", this.code);
                criterion.add(c);
            }
            if (StringUtils.isNotEmpty(this.name)) {
                Criterion c = new Criterion("name = ", this.name);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "RoleQuery{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", enable='" + enable + '\'' +
                '}';
    }
}
