package com.example.springbootdemo.datasource;

import com.example.springbootdemo.datasource.enums.DataSourceTypeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicDataSourceHolder {

    //使用ThreadLocal记录当前线程的使用的数据源
    private static final ThreadLocal<String> holder = new ThreadLocal<>();


    /**
     * 设置数据源
     */
    public static void putDataSource(DataSourceTypeEnum key) {
        log.info("==> 使用数据源: " + key.getName());
        holder.set(key.getName());
    }

    /**
     * 获取当前数据源
     */
    public static String getDataSource() {
        return holder.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        log.info("==> 移除数据源: " + holder.get());
        holder.remove();
    }
}
