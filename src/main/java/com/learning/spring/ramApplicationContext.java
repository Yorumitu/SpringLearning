package com.learning.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描——>找到BeanDefinition对象并放在Map里
 */
public class ramApplicationContext {
    //容器对应的配置
    private Class configClass;

    //Spring扫描到的bean（bean的定义，不是bean对象）都会存到这个map里面
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> singletonObjects = new HashMap<>(); // 单例池

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    //补充另一种使用Spring的方式...
    public ramApplicationContext(Class configClass) { //配置类
        this.configClass = configClass;

        scan(configClass); //找到beanDefinition对象并放到Map里

        // 实例化单例——>单例池
        // scan之后把所有单例的对象存入单例池里，原Spring还会判断不是懒加载才加入
        preInstantiateSingletons();
    }

    private void preInstantiateSingletons() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            String beanName = entry.getKey();
            if (beanDefinition.getScope().equals("singleton")) {
                //创建bean
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        //没写完
        try {

            Class clazz = beanDefinition.getClazz();

            // 实例化前处理器在这个位置工作
            // 实例化前————>对象
            // return 对象;
            // 则后续操作都不会执行，直接返回你返回的对象当作最终的bean对象

            Object instance  = clazz.newInstance();  //通过Class类的newInstance()方法创建对象（相当于使用无参构造方法）——————>原始对象

            // 依赖注入（属性赋值），对象的注入
            // 对方法：把方法全找出来，看哪些加了Autowired注解，拿到这些方法所有的参数，也是调用getBean()，通过反射的方式
            // 首先得找到属性，给加了Autowired注解的属性赋值
            for (Field field : clazz.getDeclaredFields()) { //.getDeclaredFields()获取某个类的所有声明的字段，即包括public 、private和protected
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 用反射赋值
                    // 找一个值出来赋给属性
                    String name = field.getName();
                    Object bean = getBean(name);//原Spring的getBean方法可以传入更多类型得到对象，在此只实现了通过beanName获取对象

                    if (bean == null ) { //并且@require == true时 会报错,因为此时注入对象是必要的。默认为true
                    }

                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }
            //检测@PostConstruct，初始化前执行a()方法
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    method.invoke(instance, null);
                }
            }


            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化
            // 想给某一属性赋值就可以用这种方法，Spring会自动调用我们设置的方法()
            if (instance instanceof InitializingBean) {
                // 它实现了这个接口就可以进行强转
                ((InitializingBean) instance).afterPropertiesSet();
            }

            //AOP基于Bean的后置处理器BeanPostProcessor实现
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }


            return  instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void scan(Class configClass) {
        //解析传进来的配置类
        ComponentScan annotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);//读取注解
        String path = annotation.value();   //获得扫描路径 com.learning.test 不是具体的目录
        path = path.replace(".", "/");

        //扫描-->找Component注解的类---->生成Bean对象（实例化它）
        //分两种情况：是单例bean还是多例bean
        // （寻找有某些特征的类，扫描到的类，看有没有Component注解（表示程序员告诉告诉Spring当前这个类是个bean，要去生成））
        //target目录：
        //  类加载器：
        //      boot--->jre/lib
        //      ext --->jre/ext/lib
        //      app --->classpath--->运行应用时自己指定

        ClassLoader classLoader = ramApplicationContext.class.getClassLoader(); //app
        URL resource = classLoader.getResource(path); //需要 com/learning/test，因此把上面的path中的"."换成了“/”
        File file = new File(resource.getFile());   //得到文件夹

        File[] files = file.listFiles();

        //遍历三个文件，判断类上面有没有Component注解—————>利用的class对象————>file对象转换成class对象(类加载)
        //  Spring源码不是用类加载器，而是用工具直接解析字节码，解析类上是否有Component注解，但大体思路一样
        for (File f : files) {
            //System.out.println(f.getAbsolutePath());
            //          得到H:\JavaProjects\SpringLearning\SpringLearning\target\classes\com\learning\test\AppConfig.class等
            //      ----->com\learning\test\AppConfig.class————>com.learning.test.AppConfig
            String fileName = f.getAbsolutePath();
            if (fileName.endsWith(".class")) {
                String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                className = className.replace("\\", ".");
                try {
                    Class clazz = classLoader.loadClass(className);//参数需要传想加载的类的全限定名（完整的名字）————>根据file名字得到com.learning.test.UserService
                    if (clazz.isAnnotationPresent(Component.class)) {

                        if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                            BeanPostProcessor o = (BeanPostProcessor) clazz.newInstance();
                            beanPostProcessorList.add(o);
                        }


                        // System.out.println(clazz);      //class com.learning.test.UserService
                        // 不能直接实例化，如果是多例bean，每次调用getBean()都要实例化新对象
                        // 把扫描的过程间找到的beanDefinition对象存起来
                        Component annotation1 = (Component) clazz.getAnnotation(Component.class);
                        String beanName = annotation1.value();

                        BeanDefinition beanDefinition = new BeanDefinition();

                        beanDefinitionMap.put(beanName, beanDefinition);
                        beanDefinition.setClazz(clazz);

                        if (clazz.isAnnotationPresent(Scope.class)) {
                            Scope annotation2 = (Scope) clazz.getAnnotation(Scope.class);
                            beanDefinition.setScope(annotation2.value());
                        }else {
                            beanDefinition.setScope("singleton");
                        }

                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                //System.out.println(className); // com.learning.test.AppConfig
            }


        }
    }

    public Object getBean(String beanName){ // 类名xxx————>beanDefinitionMap————>BeanDefinition对象————>scope————>单例还是多例
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                // 单例（只创建一个对象————>存到单例池里）
                // 单例对象什么时候放进单例池？——>扫描的时候
                Object o = singletonObjects.get(beanName);
                return o;
            }else {
                // 多例（每次调用时都要创建新的Bean对象）
                Object bean = createBean(beanName, beanDefinition);
                return bean;
            }


        }else {
            //传进来的名字没有beanDefinition对象
            throw new NullPointerException();
        }
    }


}
