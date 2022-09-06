package com.example.controller;

import com.example.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: springboot-shiro
 * @ClassName UserController
 * @description:
 * @author: 徐杨顺
 * @create: 2022-09-06 10:44
 * @Version 1.0
 **/
@Controller
public class UserController {
    @GetMapping("/{url}")
    public String redirect(@PathVariable("url") String url) {
        return url;
    }

    @PostMapping("/login")
    public String login(String username, String password, Model model) {
        //拿到subject
        Subject subject = SecurityUtils.getSubject();
        //将用户信息存入token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);//这个调用login会进入自定义的realm中，先认证，再授权
            model.addAttribute("msg", "登录成功");
            User user = (User) subject.getPrincipal();//获取shiro subject中的用户信息
            subject.getSession().setAttribute("user",user);
            return "index";
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            model.addAttribute("msg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            model.addAttribute("msg", "用户密码错误");
            return "login";
        }
        // return null;
    }

    @GetMapping("/unauth")
    @ResponseBody
    public String unauth() {
        return "未授权，无法访问";
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
}
