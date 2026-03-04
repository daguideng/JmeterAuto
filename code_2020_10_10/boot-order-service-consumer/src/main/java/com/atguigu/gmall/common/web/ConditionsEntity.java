package com.atguigu.gmall.common.web;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-5-23
 */
public class ConditionsEntity {
    private String operator;
    private String property ;
    private String value ;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ConditionsEntity(){}

    public ConditionsEntity(String operator, String property, String value) {
        this.operator = operator;
        this.property = property;
        this.value = value;
    }
}
