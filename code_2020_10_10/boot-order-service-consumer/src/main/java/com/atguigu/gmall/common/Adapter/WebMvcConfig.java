package com.atguigu.gmall.common.Adapter;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String webReport = "/report/**";
        String reportSource = "file:/usr/local/jmeter/mars/ReportResultDir/";
        registry.addResourceHandler(webReport).addResourceLocations(reportSource);
        super.addResourceHandlers(registry);

    }


}
