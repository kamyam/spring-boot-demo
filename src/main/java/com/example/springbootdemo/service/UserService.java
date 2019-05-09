package com.example.springbootdemo.service;

import com.example.springbootdemo.dao.UserMapper;
import com.example.springbootdemo.main.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void insert(User user) {
        userMapper.insert(user);
    }
}
