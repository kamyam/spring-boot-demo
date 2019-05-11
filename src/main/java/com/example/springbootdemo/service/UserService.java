package com.example.springbootdemo.service;

import com.example.springbootdemo.dao.test2.UserMapper2;
import com.example.springbootdemo.dao.test1.UserMapper1;
import com.example.springbootdemo.datasource.annotation.DataSource;
import com.example.springbootdemo.main.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserMapper1 userMapper1;


    @Autowired
    private UserMapper2 userMapper2;

    @DataSource("aa")
    public void insert1(User user) {
        userMapper1.insert(user);

    }

    public void insert2(User user) {
        userMapper2.insert(user);

    }
}
