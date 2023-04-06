package com.learning.test;

import com.learning.spring.BeanPostProcessor;
import com.learning.spring.Component;

/**
 * 程序员可以自己定义在初始化前后想干什么
 * 这个类产生的对象就会放到beanPostProcessorList里面
 * 我们在创建某个bean的时候，他会执行这两个方法
 *
 * 当你有个bean对象了，就可以针对它进行加工
 * 代理就是在这实现的
 * 可以直接返回代理的对象
 *
 * Spring的源码里还有个重要子接口 InstantiationAwareBeanPostProcessor，可以在bean实例化前后也进行操作
 */

@Component
public class ramBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        //如果只是想针对某个单独的bean进行处理，可以直接判断某个bean的类型、名字
        if (beanName.equals("userService")) {
            //..
            System.out.println(bean);
            System.out.println("初始化之前");
        }
        //也可以不针对某个bean，对所有的bean处理

        //甚至可以返回其他对象
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //不针对某个bean，对所有的bean处理
        System.out.println("初始化之后");

        return bean;
    }
}
