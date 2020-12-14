package com.lagou.bean;

import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class ComsumerComponet extends Thread{

    @Reference
    private HelloService  helloService;


    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final int i = ThreadLocalRandom.current().nextInt(3);
        String result = null;
        switch (i) {
            case 0:
                result = helloService.methodA("", ThreadLocalRandom.current().nextInt(100));
                break;
            case 1:
                result = helloService.methodB("", ThreadLocalRandom.current().nextInt(100));
                break;
            case 2:
                result = helloService.methodC("", ThreadLocalRandom.current().nextInt(100));
                break;
            default:
                break;

        }
        // 调用结果
        System.out.println(Thread.currentThread().getName() + " result = " + Objects.toString(result, ""));
    }



//    public String  sayHello(String name){
//        return  helloService.sayHello(name);
//    }

}
