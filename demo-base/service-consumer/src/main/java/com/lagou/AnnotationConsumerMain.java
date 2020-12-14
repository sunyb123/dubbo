package com.lagou;

import com.lagou.bean.ComsumerComponet;
import com.lagou.service.HelloService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AnnotationConsumerMain  {
    public static void main(String[] args) throws  Exception {
        System.out.println("-------------");
        AnnotationConfigApplicationContext   context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // 确保在1分钟内可以被调用 2000 次以上
//        HelloService helloService = (HelloService) context.getBean("helloService");
        for (int i = 0; i < 2300; i++) {
            ComsumerComponet  service = context.getBean(ComsumerComponet.class);
            executorService.execute(service);
        }
        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.MINUTES);



        // 获取消费者组件
//        ComsumerComponet  service = context.getBean(ComsumerComponet.class);
//        while(true){
//             System.in.read();
//             String  hello = service.sayHello("world");
//             System.out.println("result:"+hello);
//        }
    }
    @Configuration
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan(basePackages = "com.lagou.bean")
    @EnableDubbo
    static  class  ConsumerConfiguration{

    }
}
