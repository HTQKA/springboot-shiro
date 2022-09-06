package com.example.entity;

import lombok.Data;

/**
 * @program: springboot-shiro
 * @ClassName User
 * @description: 用户实体类
 * @author: 徐杨顺
 * @create: 2022-09-05 19:32
 * @Version 1.0
 **/

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String author;
    private String role;
}
