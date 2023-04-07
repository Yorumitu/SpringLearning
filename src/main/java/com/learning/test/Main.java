package com.learning.test;

import com.learning.spring.ramApplicationContext;

/**
 * Bean创建的生命周期：
 *  Spring基于UserService的无参构造方法—————>反射调用————>对象————依赖注入（@Autowired）
 *       ————>初始化前(@PostConstruct)————>初始化(实现接口InitializingBean，实现了就强转、调用接口的方法)
 *           —————>初始化后（AOP）（————>代理对象）
 *           [代理对象生成是在最后，之后没有其他步骤了，也不会再针对代理对象进行依赖注入，所以代理对象生成之后里面的属性是空的，要靠后续步骤]
 *           [代理对象的生成：对UserService而言（也没有实现什么接口），使用的xxxx生成代理对象，而xxx是通过父子类的机制生成代理对象，即先生成代理类，]
 *               UserServiceProxy对象————>UserService代理对象————>UserService代理对象.target = 普通对象 ————> 完成（放单例池里）
 *
 *               UserService代理对象.test()————>先执行子类的————>再执行父类的test方法
 *               只是在调用执行方法，里面的orderService属性没有赋值
 *
 *               class UserServiceProxy extends UserService{
 *
 *                   UserService target;
 *
 *                   public void test(){
 *
 *                       //先执行@Before切面逻辑
 *                       //再执行UserService里的test方法：target.test()
 *                   }
 *               }
 *            ————>AOP会先生成代理类，再根据这个类产生代理对象，再给代理对象里的target属性赋值，赋普通对象的值（普通对象和代理对象都在JVM里，代理对象后续会放入单例池），然后再执行代理对象的test()，执行完再执行切面逻辑，之后再执行target的test()
 *            此时该有值的属性也就都有值了，并且由于target指向普通对象，普通对象也不会被垃圾回收掉
 *               ————>放入单例池Map（单例的话，多例直接创建对象返回）————>Bean对象
 */
public class Main {
    public static void main(String[] args) {
        //用Spring. 测试
        ramApplicationContext ramApplicationContext = new ramApplicationContext(AppConfig.class);


        UserService userService = (UserService) ramApplicationContext.getBean("userService");


        userService.test();
    }
}
