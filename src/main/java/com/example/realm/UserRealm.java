package com.example.realm;

import com.example.entity.User;
import com.example.serivce.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: springboot-shiro
 * @ClassName UserRealm
 * @description: 用户realm类，用于认证和授权
 * @author: 徐杨顺
 * @create: 2022-09-05 20:57
 * @Version 1.0
 **/
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    /**
     * 用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前用户登录信息
        Subject subject = SecurityUtils.getSubject();
        //之前登录时已经将用户信息存入subject，所以现在得到的subject包含用户信息
        User user = (User) subject.getPrincipal();
        //设置权限和角色，就是将数据库中的该用户拥有的角色，权限复制给Shiro的角色权限管理器
        //设置角色
        Set<String> roles = new HashSet<>();//使用set集合，避免重复的角色
        roles.add(user.getRole());
        //AuthorizationInfo，角色的权限信息集合，授权时使用。
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);

        //设置权限
        info.addStringPermission(user.getAuthor());//将数据库中用户的权限
        return info;
    }

    /**
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     * 用户认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //将用户信息封装一个token，包括用户名和密码
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //通过Service查询用户信息，得到一个User对象
        User user = userService.findByUsername(token.getUsername());
        //判断用户名是否存在
        if (user != null) {
            //用户存在
            //将查询到的对象，对象密码，和用户输入的密码交给shiro来判断用户密码是否正确
            return  new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        }
        //用户不存在，直接结束验证
        return null;
    }
}
