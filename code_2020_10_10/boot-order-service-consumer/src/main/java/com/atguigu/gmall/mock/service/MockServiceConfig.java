package com.atguigu.gmall.mock.service;


import com.atguigu.gmall.entity.Mock_config;
import com.atguigu.gmall.mock.common.web.MockConfigQuery;

import java.util.List;
import java.util.Map;

import org.apache.activemq.broker.jmx.MBeanInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-7
 */

@Mapper
public interface MockServiceConfig {

    public Map<String, Object> list(MockConfigQuery query);

    public String saveMockConfigData(Mock_config mockconfig);

    public String editMockConfigData(Mock_config  mockconfig);

    public String doStatusMockConfigData(String  id);

    public List<Mock_config> interceptorList(String url);

}
