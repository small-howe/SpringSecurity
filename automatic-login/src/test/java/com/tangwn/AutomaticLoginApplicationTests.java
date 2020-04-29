package com.tangwn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@SpringBootTest
class AutomaticLoginApplicationTests {

    /**
     *
     * remember-me=
     */
    @Test
    void contextLoads() throws UnsupportedEncodingException {
        String s = new String(Base64.getDecoder().decode("aG93ZToxNTg5MzUwMzU2NTY0OmEyNmFkZmI5NDMwNjdkZjQxOWZjMjIxMWVmNTYyYzgz"), "UTF-8");
        System.out.println("s = " + s);
    }

}
