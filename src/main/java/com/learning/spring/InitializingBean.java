package com.learning.spring;

public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
