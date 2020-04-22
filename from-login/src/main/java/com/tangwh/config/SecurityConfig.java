package com.tangwh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
     * 配置登录信息
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置用户名和密码 (在内存中配置)
        auth.inMemoryAuthentication()
                .withUser("javaboy")
                .password("123456")
                .roles("admin");
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
                //任何请求 需要登录后 才能访问
                .anyRequest().authenticated()
                .and()
                //表单配置
                .formLogin()
                //登录页面(隐含 的意思 登录请求也是这个)
                .loginPage("/login.html")
                //配置登录接口
                .loginProcessingUrl("/doLogin")
                //可以修改 登录时候 name 属性框的name
//                .usernameParameter("name")
                //可以修改 登录时候 password 属性框的name
//                .passwordParameter("pwd")
                // 登录成功的跳转地址(服务端跳转) 不管从哪个页面来的 经过登录后 跳转到/s的页面
//                .successForwardUrl("/s")
                // 登录成功的跳转地址(客户端跳转重定向)
                .defaultSuccessUrl("/s")
                //登录失败的跳转 服务端跳转
//                .failureForwardUrl("/s")
                //登录失败的跳转 客户端跳转(重定向)
//                .failureUrl("/s")
                //跟登录相关的页面 都放心
                .permitAll()
                .and()
                //注销登录
                .logout()
                //注销的地址  GET 请求
                .logoutUrl("/logout")
                // 注销地址 Post请求
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                // 注销成功去哪里?
                .logoutSuccessUrl("/login.html")
                // 是否让Session 失效 默认 失效
//                .invalidateHttpSession(true)
                // 清除认证信息  默认 清除
//                .clearAuthentication(true)
                //相关的URL 放行
                .permitAll()
                .and()
                //关闭csrf
                .csrf().disable();

    }


    /**
     * 放心 css
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }
}
