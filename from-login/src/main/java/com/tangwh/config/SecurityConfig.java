package com.tangwh.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 不对密码进行加密
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder(){

        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 配置登录信息 1
     *
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 配置用户名和密码 (在内存中配置)
//        auth.inMemoryAuthentication()
//                .withUser("javaboy")
//                .password("123456")
//                .roles("admin")
//                .and()
//                //添加多用户
//                .withUser("江南一点雨")
//                .password("123")
//                .roles("user");
//    }


    /**
     * 配置用户信息 二
     *  基于内存的UserDetailsManager
     * @return
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.withUsername("javaboy").password("123").roles("admin").build());
        manager.createUser(User.withUsername("江南一点雨").password("123").roles("user").build());

        return manager;

    }


    /**
     * 角色继承
     * @return
     */
    @Bean
    RoleHierarchy roleHierarchy(){

        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        //角色继承
        hierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return hierarchy;
    }

    /**
     * 配置登录页面
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 配置拦截规则 如果你的访问格式 是admin
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasRole("user")


                //任何请求 需要登录后 才能访问
                .anyRequest().authenticated()
                .and()
                //表单配置
                .formLogin()
                //登录页面(隐含 的意思 登录请求也是这个) -- 前后端不分离
//                .loginPage("/login.html")
                //配置登录接口
                .loginProcessingUrl("/doLogin")
                //可以修改 登录时候 name 属性框的name
//                .usernameParameter("name")
                //可以修改 登录时候 password 属性框的name
//                .passwordParameter("pwd")
                // 登录成功的跳转地址(服务端跳转)      -----前后端不分离  不管从哪个页面来的 经过登录后 跳转到/s的页面
//                .successForwardUrl("/s")
                // 登录成功的跳转地址(客户端跳转重定向) -----前后端不分离
//                .defaultSuccessUrl("/s")
                // 登录成功后 的函数回调              ----前后端分离
                .successHandler((req, resp, authentication) -> {

                    //req -- 服务端跳转
                    //resp-- 客户端挑战 返回JSON
                    //authentication-- 当前登录成功的用户信息
                    resp.setContentType("application/json;charset=utf-8");
                    // 返回JSON 返回当前用户的信息
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(authentication.getPrincipal()));

                    out.flush();
                    out.close();


                })
                //登录失败的跳转 服务端跳转  -- 前后端不分离
//                .failureForwardUrl("/s")
                //登录失败的跳转 客户端跳转(重定向) -- 前后端不分离
//                .failureUrl("/s")
                //登录失败的 回调  ---前后端分离
                .failureHandler((req, resp, exception) -> {

                    //req -- 服务端跳转
                    //resp-- 客户端挑战 返回JSON
                    //authentication-- 当前登录成功的用户信息
                    resp.setContentType("application/json;charset=utf-8");
                    // 返回JSON 返回当前用户的信息
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(exception.getMessage()));

                    out.flush();
                    out.close();


                })
                //跟登录相关的页面 都放行
                .permitAll()
                .and()
                //注销登录
                .logout()
                //注销的地址  GET 请求
                .logoutUrl("/logout")
                // 注销地址 Post请求 -- 前后端不分离
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                // 注销成功去哪里?    -- 前后端不分离
//                .logoutSuccessUrl("/login.html")
                .logoutSuccessHandler((req,resp,authentication)->{


                    //req -- 服务端跳转
                    //resp-- 客户端挑战 返回JSON
                    //authentication-- 当前登录成功的用户信息
                    resp.setContentType("application/json;charset=utf-8");
                    // 返回JSON 返回当前用户的信息
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString("注销登录成功"));

                    out.flush();
                    out.close();


                })
                // 是否让Session 失效 默认 失效
//                .invalidateHttpSession(true)
                // 清除认证信息  默认 清除
//                .clearAuthentication(true)
                //相关的URL 放行
                .permitAll()
                .and()
                //关闭csrf
                .csrf().disable()
                //未认证 的请求  -- 前后端分离
                .exceptionHandling()
                .authenticationEntryPoint((req,resp,exception)->{

                    //req -- 服务端跳转
                    //resp-- 客户端挑战 返回JSON
                    //authentication-- 当前登录成功的用户信息
                    resp.setContentType("application/json;charset=utf-8");
                    // 返回JSON 返回当前用户的信息
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString("尚未登录,请登录"));

                    out.flush();
                    out.close();
                });

    }


    /**
     * 放行 css
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }
}
