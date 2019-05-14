package com.example.springbootdemo.spring;

import org.apache.commons.lang3.StringUtils;

public interface ApiVersionConstant {

    String V5_4_1 = "5.4.1";

    String V5_4_2 = "5.4.1";


    static int getIntVersion(String version){
        if (StringUtils.isBlank(version)) return 0;
        return Integer.valueOf(version.replace(".", ""));
    }
}
