package com.atguigu.gmall.common.web;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SystemSettingQuery extends QueryBase {


    private String id;
    private String settingKey;
    private String settingValue;
    private String description;
    private String menuType;
    private Boolean enabled;


    @Override
    public List<Criterion> getCriterion() {
        List<Criterion> criterion = new ArrayList<>();

        if (StringUtils.isNotEmpty(this.search)) {
            if (StringUtils.isNotEmpty(this.menuType)) {
                Criterion c = new Criterion("menuType = ", this.menuType);
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

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "SystemSettingQuery{" +
                "id='" + id + '\'' +
                ", settingKey='" + settingKey + '\'' +
                ", settingValue='" + settingValue + '\'' +
                ", description='" + description + '\'' +
                ", menuType='" + menuType + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

