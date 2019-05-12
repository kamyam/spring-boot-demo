package com.example.springbootdemo.service1;

import com.example.springbootdemo.dao.UserMapper;
import com.example.springbootdemo.datasource.annotation.DynamicDataSource;
import com.example.springbootdemo.datasource.enums.DataSourceTypeEnum;
import com.example.springbootdemo.main.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {


    @Autowired
    private UserMapper userMapper;

    @DynamicDataSource(DataSourceTypeEnum.TEST1)
    public void insert1(User user) {
        userMapper.insert(user);

    }
    @DynamicDataSource(DataSourceTypeEnum.TEST2)
    public void insert2(User user) {
        userMapper.insert(user);

    }
}
