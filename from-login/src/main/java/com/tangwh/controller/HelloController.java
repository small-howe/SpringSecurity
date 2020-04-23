package com.tangwh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping("/hello")
    public String hello(){
        return "Hello Word";
    }


    /**
     * 需要具备 admin 身份的人访问
     * @return
     */
    @GetMapping("/admin/hello")
    public String admin() {

        return "admin";
    }


    /**
     * 需要具备 user的 身份的人访问
     * @return
     */
    @GetMapping("/user/hello")
    public String user() {

        return "user";
    }


}
