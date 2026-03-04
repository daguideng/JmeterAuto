package com.atguigu.gmall.mock.common.web;


import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-14
 */
public class MockConfigQuery extends QueryBase implements Serializable {
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

    public MockConfigQuery(){}




    public MockConfigQuery(String mockName, String requestType ,String mockUrl, String prefixOption, String configRules, String weight, String resultType, String labe, String otherMockService, String mockResult, String tag, String timeout, Date createTime) {
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

    public MockConfigQuery(String mockName, String requestType, String mockUrl, String prefixOption, String configRules, String weight, String resultType, String labe, String otherMockService, String mockResult, String tag, String timeout) {
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

    public MockConfigQuery(String mockName, String requestType, String mockUrl, String prefixOption, String configRules, String weight, String resultType, String labe, String otherMockService, String mockResult, String tag, String timeout, Date createTime, Date updateTime) {
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
                ", mockUrl='" + mockUrl + '\'' +
                ", prefixOption='" + prefixOption + '\'' +
                ", configRules='" + configRules + '\'' +
                ", weight='" + weight + '\'' +
                ", resultType='" + resultType + '\'' +
                ", labe='" + labe + '\'' +
                ", otherMockService='" + otherMockService + '\'' +
                ", mockResult='" + mockResult + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }






    @Override
    public List<Criterion> getCriterion() {
        List<Criterion> criterion = new ArrayList<>();
        if (StringUtils.isNotEmpty(this.search)) {
            if (this.mockName != null) {
                Criterion c = new Criterion("mock_name like ", "%" + this.mockName + "%");
                criterion.add(c);
            }

            if (this.mockUrl != null) {
                Criterion c = new Criterion("mock_url like ", "%" + this.mockUrl + "%");
                criterion.add(c);
            }

        }

        return criterion.isEmpty() ? null : criterion;
    }


    @Override
    public PageBounds getPB() {
        this.setProperty("id");
        this.setDirection(Order.Direction.DESC);
        return super.getPB();
    }


    public PageBounds getPBAsc() {
        this.setProperty("id");
        this.setDirection(Order.Direction.ASC);
        return super.getPB();
    }


}
