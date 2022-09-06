package com.example.serivce;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: springboot-shiro
 * @ClassName UserService
 * @description:
 * @author: 徐杨顺
 * @create: 2022-09-05 19:38
 * @Version 1.0
 **/
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User findByUsername(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }
}
