package com.example.springbootdemo.controller;

import com.example.springbootdemo.main.User;
import com.example.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/insert")
    public void insert(){
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setAge(new Random().nextInt(100));
        user.setName(Thread.currentThread().getName());

        userService.insert(user);
    }
}
