package com.example.springbootdemo.spring.condition;

import com.example.springbootdemo.spring.ApiVersionConstant;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    // 匹配版本
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("(\\d{1,2}.\\d{1,2}.\\d{1,2})");

    private int apiVersion;

    public ApiVersionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(httpServletRequest.getHeader("version"));
        if(m.find() && ApiVersionConstant.getIntVersion(m.group(1)) >= this.apiVersion){
            // 如果请求的版本号大于配置版本号， 则满足
            return this;
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        // 优先匹配最新的版本号
        return apiVersionCondition.getApiVersion() - this.apiVersion;
    }
}
