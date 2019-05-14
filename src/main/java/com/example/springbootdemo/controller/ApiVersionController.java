package com.example.springbootdemo.controller;


import com.example.springbootdemo.annotation.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiVersionController {

    @ApiVersion("5.4.1")
    @RequestMapping("/getVersion")
    public String getVersion1(){
        return "5.4.1";
    }


    @ApiVersion("5.4.2")
    @RequestMapping("/getVersion")
    public String getVersion2(){
        return "5.4.2";
    }
}
