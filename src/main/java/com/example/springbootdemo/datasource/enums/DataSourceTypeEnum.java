package com.example.springbootdemo.datasource.enums;

import java.util.Arrays;
import java.util.List;

public enum DataSourceTypeEnum {

    TEST1, TEST2;


    public String getName() {
        return this.name().toLowerCase();
    }


    public static List<DataSourceTypeEnum> getDataSourceTypes() {
        return Arrays.asList(values());
    }

    public static DataSourceTypeEnum getDefaultDataSourceType() {
        return TEST1;
    }
}
