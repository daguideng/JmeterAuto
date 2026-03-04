package com.atguigu.gmall.entity;

import java.util.Date;
import java.util.Objects;

public class Mock_config {
    private Integer id;

    private String mockName;

    private String requestType;

    private String mockUrl;

    private String prefixOption;

    private String configRules;

    private String weight;

    private String resultType;

    private String labe;

    private String otherMockService;

    private String mockResult;

    private String tag;

    private String timeout ;

    private Date createTime;

    private Date updateTime;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMockName() {
        return mockName;
    }

    public void setMockName(String mockName) {
        this.mockName = mockName == null ? null : mockName.trim();
    }

    public String getMockUrl() {
        return mockUrl;
    }

    public void setMockUrl(String mockUrl) {
        this.mockUrl = mockUrl == null ? null : mockUrl.trim();
    }

    public String getPrefixOption() {
        return prefixOption;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public void setPrefixOption(String prefixOption) {
        this.prefixOption = prefixOption == null ? null : prefixOption.trim();
    }

    public String getConfigRules() {
        return configRules;
    }

    public void setConfigRules(String configRules) {
        this.configRules = configRules == null ? null : configRules.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType == null ? null : resultType.trim();
    }

    public String getLabe() {
        return labe;
    }

    public void setLabe(String labe) {
        this.labe = labe == null ? null : labe.trim();
    }

    public String getOtherMockService() {
        return otherMockService;
    }

    public void setOtherMockService(String otherMockService) {
        this.otherMockService = otherMockService == null ? null : otherMockService.trim();
    }

    public String getMockResult() {
        return mockResult;
    }

    public void setMockResult(String mockResult) {
        this.mockResult = mockResult == null ? null : mockResult.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Mock_config(){}


    public Mock_config(Integer id, String mockName, String requestType, String mockUrl, String prefixOption, String configRules, String weight, String resultType, String labe, String otherMockService, String mockResult, String tag, String timeout) {
        this.id = id;
        this.mockName = mockName;
        this.requestType = requestType;
        this.mockUrl = mockUrl;
        this.prefixOption = prefixOption;
        this.configRules = configRules;
        this.weight = weight;
        this.resultType = resultType;
        this.labe = labe;
        this.otherMockService = otherMockService;
        this.mockResult = mockResult;
        this.tag = tag;
        this.timeout = timeout;
    }

    public Mock_config(String mockName, String requestType , String mockUrl, String prefixOption, String configRules, String weight, String resultType, String labe, String otherMockService, String mockResult, String tag, String timeout, Date createTime) {
        this.mockName = mockName;
        this.requestType = requestType ;
        this.mockUrl = mockUrl;
        this.prefixOption = prefixOption;
        this.configRules = configRules;
        this.weight = weight;
        this.resultType = resultType;
        this.labe = labe;
        this.otherMockService = otherMockService;
        this.mockResult = mockResult;
        this.tag = tag;
        this.timeout = timeout ;
        this.createTime = createTime;
    }

    public Mock_config(String mockName, String requestType, String mockUrl, String prefixOption, String configRules, String weight, String resultType, String labe, String otherMockService, String mockResult, String tag, String timeout) {
        this.mockName = mockName;
        this.requestType = requestType;
        this.mockUrl = mockUrl;
        this.prefixOption = prefixOption;
        this.configRules = configRules;
        this.weight = weight;
        this.resultType = resultType;
        this.labe = labe;
        this.otherMockService = otherMockService;
        this.mockResult = mockResult;
        this.tag = tag;
        this.timeout = timeout;
    }

    public Mock_config(String mockName, String requestType, String mockUrl, String prefixOption, String configRules, String weight, String resultType, String labe, String otherMockService, String mockResult, String tag, String timeout, Date createTime, Date updateTime) {
        this.mockName = mockName;
        this.requestType = requestType ;
        this.mockUrl = mockUrl;
        this.prefixOption = prefixOption;
        this.configRules = configRules;
        this.weight = weight;
        this.resultType = resultType;
        this.labe = labe;
        this.otherMockService = otherMockService;
        this.mockResult = mockResult;
        this.tag = tag;
        this.timeout = timeout ;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        return "Mock_config{" +
                "mockName='" + mockName + '\'' +
                ", requestType='" + requestType + '\'' +
                ", mockUrl='" + mockUrl + '\'' +
                ", prefixOption='" + prefixOption + '\'' +
                ", configRules='" + configRules + '\'' +
                ", weight='" + weight + '\'' +
                ", resultType='" + resultType + '\'' +
                ", labe='" + labe + '\'' +
                ", otherMockService='" + otherMockService + '\'' +
                ", mockResult='" + mockResult + '\'' +
                ", tag='" + tag + '\'' +
                ", timeout='" + timeout + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mock_config)) return false;
        Mock_config that = (Mock_config) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getMockName(), that.getMockName()) &&
                Objects.equals(getMockUrl(), that.getMockUrl()) &&
                Objects.equals(getPrefixOption(), that.getPrefixOption()) &&
                Objects.equals(getConfigRules(), that.getConfigRules()) &&
                Objects.equals(getWeight(), that.getWeight()) &&
                Objects.equals(getResultType(), that.getResultType()) &&
                Objects.equals(getLabe(), that.getLabe()) &&
                Objects.equals(getOtherMockService(), that.getOtherMockService()) &&
                Objects.equals(getMockResult(), that.getMockResult()) &&
                Objects.equals(getTag(), that.getTag()) &&
                Objects.equals(getCreateTime(), that.getCreateTime()) &&
                Objects.equals(getUpdateTime(), that.getUpdateTime());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getMockName(), getMockUrl(), getPrefixOption(), getConfigRules(), getWeight(), getResultType(), getLabe(), getOtherMockService(), getMockResult(), getTag(), getCreateTime(), getUpdateTime());
    }
}