package com.learning.test;

import com.learning.spring.ramApplicationContext;

public class Main {
    public static void main(String[] args) {
        //用Spring. 测试
        ramApplicationContext ramApplicationContext = new ramApplicationContext(AppConfig.class);

        UserService userService = (UserService) ramApplicationContext.getBean("userService");
//
//        System.out.println(userService);
        userService.test();
    }
}
