package com.example.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.example.realm.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Hashtable;
import java.util.Map;

/**
 * @program: springboot-shiro
 * @ClassName ShiroConfig
 * @description: Shiro配置类
 * @author: 徐杨顺
 * @create: 2022-09-06 10:09
 * @Version 1.0
 **/
@Configuration
public class ShiroConfig {

    /**
     * 第三步：hiroFilterFactoryBean，过滤器工厂，Shiro基本运行机制就是开发者定制规则，Shiro去执行，
     * 具体的执行操作就是由ShiroFilterFactoryBean创建的一个个Filter对象来完成。
     *  @Bean将ShiroFilterFactoryBean注入到ioc容器中，@Qualifier("defaultWebSecurityManager")通过ioc容器把注入完成的DefaultWebSecurityManager拿来使用
     * @param manager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(manager);
        //认证和授权，权限设置
        Map<String, String> map = new Hashtable<>();
        map.put("/main", "authc");//mian页面必须登录，才能访问
        map.put("/manage", "perms[manage]");//manage页面，需要拥有manage权限才可以访问
        map.put("/admin","roles[admin]");//admin页面，必须拥有admin角色才可以访问
        factoryBean.setFilterChainDefinitionMap(map);//shiro根据map设置过滤器
        //设置登录页面
        factoryBean.setLoginUrl("/login");
        /*
        当shiro权限认证不通过时，会默认条状到login.jsp他页面。这里自定也设置login登陆页面，会先去controller查到login请求，然后根据试图解析器去查找tamplates对应的html文件
         */
        //设置未授权页面
        factoryBean.setUnauthorizedUrl("/unauth");

        return factoryBean;
    }

    //第二步，注入安全管理器DefaultWebSecurityDManager，自定义realm需要注册到DefaultWebSecurityDManager中才可以使用
    //UserRealm 注入到ioc容器之后，通过@Qualifier("userRealm") UserRealm userRealm 取出来注册到DefaultWebSecurityManager中
    //@Qualifier("userRealm") bean的名字就是方法名（通过方法来返回一个对象）
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();//
        manager.setRealm(userRealm);//注册自定义的realm
        return manager;
    }

    //第一步将自定义的realm注入到ioc容器中
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    //加载shiro方言，用于整合thymeleaf
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
