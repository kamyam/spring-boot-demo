package com.example.springbootdemo.spring.mapping;

import com.example.springbootdemo.annotation.ApiVersion;
import com.example.springbootdemo.spring.ApiVersionConstant;
import com.example.springbootdemo.spring.condition.ApiVersionCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    //类上的注解
    @Override
    protected RequestCondition getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(apiVersion);
    }

    //方法上的注解
    @Override
    protected RequestCondition getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }


    private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionCondition(ApiVersionConstant.getIntVersion(apiVersion.value()));
    }

}
