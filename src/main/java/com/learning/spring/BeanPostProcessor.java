package com.learning.spring;

/**
 * Spring提供给程序员的扩展机制（在初始化之前、之后做什么事），程序员想干什么都可以用这个来实现，
 *
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);
}
