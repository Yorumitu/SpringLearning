package com.learning.test;

import com.learning.spring.Autowired;
import com.learning.spring.Component;
import com.learning.spring.InitializingBean;
import com.learning.spring.Scope;

@Component("userService")
public class UserService implements InitializingBean {  // Spring扫描到这个类，根据类上的注解生成BeanDefinition对象
                            // BeanDefinition————>Map<'beanName', BeanDefinition对象>
    @Autowired
    private OrderService orderService;

    private User defaultUser;

    public void test() { //测试依赖注入
        System.out.println(orderService);
        System.out.println(defaultUser);
    }

    //Spring里的初始化，如果你想给某个属性赋值，就可以这么做
    //
    @Override
    public void afterPropertiesSet() throws Exception {
        //从数据库里找到默认用户信息
        //mysql———>User———>
        defaultUser = new User();
    }
}
