package com.learning.test;

import com.learning.spring.Autowired;
import com.learning.spring.Component;
import com.learning.spring.InitializingBean;
import com.learning.spring.Scope;

import javax.annotation.PostConstruct;

@Component("userService")
public class UserService implements InitializingBean {  // Spring扫描到这个类，根据类上的注解生成BeanDefinition对象
                            // BeanDefinition————>Map<'beanName', BeanDefinition对象>
    @Autowired
    private OrderService orderService;  // byType———>byName，同下面构造方法入参执行的过程

    private User defaultUser;

    private User Admin;

    //类中只有一个构造方法，Spring会调用它；
    // 如果有多个，会调用默认的无参构造方法；
    // 如果有多个构造方法，但没有无参的构造方法，会报错
    //   在想要执行的构造方法上加@Autowired也可以
    public UserService () {

    }

    //Spring使用时入参有值，要先获取OrderService的Bean对象（@Component）
    // 单例池Map<'orderService', OrderService对象> ，先根据类型来找对象（遍历这个Map）
    // 此时能找到三个OrderService类型的对象（我们定义的），三选一：再用名字来找
    //     即先byType（找到一个就是它了，有多个再靠名字）——>再byName(找不到会报错)
    public UserService (OrderService orderService) {
        this.orderService = orderService;
    }


    public void test() { //测试依赖注入
        System.out.println(orderService);
        System.out.println(defaultUser);
    }

    //Spring里的初始化，如果你想给某个属性赋值，就可以这么做
    //
    @Override
    public void afterPropertiesSet() throws Exception {
        //从数据库里找到默认用户信息
        //mysql———>User———>this.defaultUser
        defaultUser = new User();
    }

    @PostConstruct
    public void a() {
        //mysql————>User————>this.admin
    }
}
