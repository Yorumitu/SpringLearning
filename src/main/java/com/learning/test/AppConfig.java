package com.learning.test;

import com.learning.spring.Bean;
import com.learning.spring.ComponentScan;
import com.learning.spring.AOP.EnableAspectJAutoProxy;


/**
 * 用户的配置类
 *
 * Spring事务
 * AOP 代理类
 *               class UserServiceProxy extends UserService{
 *
 *                   UserService target;
 *
 *                   public void test(){
 *
 *                       //@Transaction注解 （这是代理带来的额外的操作，只有在代理对象执行时候才会去判断有没有这个注解）
 *                       //1. 事务管理器兴建一个数据连接conn
 *                       //2. conn.autocommit = false //我们希望他们能一起提交或者一起回滚
 *                       // target.test() sql1, sql2
 *                       //3. conn.rollback, conn.commit //成功就一起提交，不然就一起回滚
 *                   }
 *               }
 *
 *
 *
 */
@ComponentScan("com.learning.test")
@EnableAspectJAutoProxy
public class AppConfig {

//    @Bean
//    public JdbcTemplete jdbcTemplete(){
//        return JdbcTemplete(dataSource());
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(){
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
//        transactionManager.setDataSource(dataSouce());
//        return transactionManager;
//    }
//
//    public DataSource dataSouce(){
//        DriverManagerDataSource dataSource = new DriverMangerDataSource();
//        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test");
//        dataSource.setUsername("root");
//        dataSource.serPassWord("wcx135");
//        return dataSource;
//    }


//    //相当于定义了三个OrderService类型的对象，他们的名字不一样，
//    // 我们还定义了一个OrderService的单例Bean，这不代表只能有一个OrderService类型的对象
//    @Bean
//    public OrderService orderService1() {
//        return new OrderService();
//    }
//    @Bean
//    public OrderService orderService2() {
//        return new OrderService();
//    }

}
