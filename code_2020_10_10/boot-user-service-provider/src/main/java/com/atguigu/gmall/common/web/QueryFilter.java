package com.atguigu.gmall.common.web;


public class QueryFilter {
    /**
     * 等于
     */
    public static final String COMPARISION_EQ = "eq";
    /**
     * 小于
     */
    public static final String COMPARISION_LT = "lt";
    
    /**
     * 小于等于
     */
    public static final String COMPARISION_LE ="le";

    /**
     * 大于
     */
    public static final String COMPARISION_GT = "gt";
    
    /**
     * 大于等于
     */
    public static final String COMPARISION_GE ="ge";
    /**
     * boolean型
     */
    public static final String TYPE_BOOLEAN = "==";
    public static final String TYPE_LIST = "in";
    public static final String COMPARISION_LIKE = "like";

    /**
     * 比较规则符号
     */
    private String operator;

    /**
     * 列名
     */
    private String property;

    /**
     * 值
     */
    private Object value;

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

	@Override
	public String toString() {
		return "QueryFilter [operator=" + operator + ", property=" + property + ", value=" + value + "]";
	}
    
    

}
