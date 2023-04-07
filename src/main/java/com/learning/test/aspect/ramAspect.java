package com.learning.test.aspect;


import com.learning.spring.AOP.Aspect;
import com.learning.spring.AOP.Before;
import com.learning.spring.Component;
import com.learning.spring.JointPoint;

@Aspect
@Component
public class ramAspect {

    @Before("execution(public void com.learning.test.UserService.test())")
    //表示执行上面的test方法之前要执行这个方法
    public void ramBefore(JointPoint jointPoint){
        System.out.println("ramBefore");
    }
}
