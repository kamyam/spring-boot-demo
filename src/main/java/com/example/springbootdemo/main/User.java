package com.example.springbootdemo.main;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class User {
    private String id;
    private String name;
    private int age;
}
