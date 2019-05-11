package com.example.springbootdemo.datasource.annotation;

import com.example.springbootdemo.datasource.enums.DataSourceTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态数据源注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DynamicDataSource {
    DataSourceTypeEnum value();
}
