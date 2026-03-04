package com.atguigu.gmall.common.page;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;



public  class Criterion {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String condition;

    private Object value;

    private Object secondValue;

    /**Criterion不包含值比较*/
    private boolean noValue;
    /**Criterion包含单一值比较,=*/
    private boolean singleValue;
    /**Criterion包含两个值得比较,value&secondValue*/
    private boolean betweenValue;
    /**Criterion包含list比较,输入值在listValue中*/
    private boolean listValue;

    public String getCondition() {
        return this.condition;
    }

    public Object getValue() {
        return this.value;
    }

    public Object getSecondValue() {
        return this.secondValue;
    }

    public boolean isNoValue() {
        return this.noValue;
    }

    public boolean isSingleValue() {
        return this.singleValue;
    }

    public boolean isBetweenValue() {
        return this.betweenValue;
    }

    public boolean isListValue() {
        return this.listValue;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setSecondValue(Object secondValue) {
        this.secondValue = secondValue;
    }

    public void setNoValue(boolean noValue) {
        this.noValue = noValue;
    }

    public void setSingleValue(boolean singleValue) {
        this.singleValue = singleValue;
    }

    public void setBetweenValue(boolean betweenValue) {
        this.betweenValue = betweenValue;
    }

    public void setListValue(boolean listValue) {
        this.listValue = listValue;
    }

    public Criterion(String condition) {
        super();
        this.condition = condition;
        this.noValue = true;
    }

    /**Criterion构造函数,单值比较,list比较,复杂查询条件like...*/
    public Criterion(String condition, Object value) {
    
        super();
        // logger.info("根据CONDITION: {} 和 VALUE:{} 生成 Criterion",condition,value);
    	
        this.condition = condition;
        this.value = value;
        if (value instanceof List<?>) {
            this.listValue = true;
        } else {
            this.singleValue = true;
        }
    }
    
    /**Criterion构造函数,2值比较*/
    public Criterion(String condition, Object value, Object secondValue) {
        super();
        this.condition = condition;
        this.value = value;
        this.secondValue = secondValue;
        this.betweenValue = true;
    }

	@Override
	public String toString() {
		return "Criterion [condition=" + condition + ", value=" + value + ", secondValue=" + secondValue + ", noValue="
				+ noValue + ", singleValue=" + singleValue + ", betweenValue=" + betweenValue + ", listValue="
				+ listValue + "]";
	}
    
    

}