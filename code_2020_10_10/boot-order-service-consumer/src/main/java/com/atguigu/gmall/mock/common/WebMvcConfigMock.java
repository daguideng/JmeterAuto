package com.atguigu.gmall.mock.common;

/**
 * WebMVC配置，你可以集中在这里配置拦截器、过滤器、静态资源缓存等
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;


@Configuration
public  class WebMvcConfigMock implements WebMvcConfigurer {

    private static final String PATHS = "/**";

    @Autowired
    private RequestInterceptor requestInterceptor ;




    @Override
    public void addInterceptors(InterceptorRegistry registry) {

            registry.addInterceptor(requestInterceptor).addPathPatterns(PATHS).
                    excludePathPatterns("/user/**", "/actuator/**", "/mock/**", "/timer/**",
                            "/jmeterperf/**", "/jmeterinter/**", "/mars/**", "/webdownloadFile/**",
                            "/agentsource/**", "/report/**", "/wang/**", "/websocket/**","/agentstatus/**","/mars/**/**/**");



//        registry.addInterceptor(requestInterceptor).addPathPatterns(PATHS).
//                excludePathPatterns("/user/**","/actuator/**","/mock/**","/timer/**",
//                        "/jmeterperf/**","/jmeterinter/**","/mars/**","/webdownloadFile/**",
//                        "/agentsource/**","/error","/report/**","/wang/**","/websocket/**");
      //  registry.addInterceptor(new RequestInterceptor()).addPathPatterns(PATHS);
        // registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
        // super.addInterceptors(registry);
    }



    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {

    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {

    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {

    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {

    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {

    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }


}