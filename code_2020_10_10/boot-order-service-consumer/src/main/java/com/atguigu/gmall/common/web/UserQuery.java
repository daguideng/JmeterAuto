package com.atguigu.gmall.common.web;

import com.github.miemiedev.mybatis.paginator.domain.Order.Direction;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 */
public class UserQuery extends QueryBase {
    private String id;
    private String roleId;

    /*
     * (non-Javadoc)
     * @see com.ucredit.wit.common.page.QueryBase#getCriterion()
     */
    @Override
    public List<Criterion> getCriterion() {
        List<Criterion> criterion = new ArrayList<>();

        if (StringUtils.isNotEmpty(this.search)) {
            if (StringUtils.isNotEmpty(this.roleId)) {
                Criterion c = new Criterion("roleId = ", this.roleId);
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
        this.setDirection(Direction.ASC);
        return super.getPB();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserQuery{" +
                "id='" + id + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
