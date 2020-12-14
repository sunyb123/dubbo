package com.lagou.service.impl;

import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Service;

import java.util.concurrent.TimeUnit;

@Service
public class HelloServiceImpl   implements HelloService {

    @Override
    public String methodA(String name, int mills) {
        try {
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method A : " + name);
        return "method A : " + name;
    }

    @Override
    public String methodB(String name, int mills) {
        try {
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method B : " + name);
        return "method B : " + name;
    }

    @Override
    public String methodC(String name, int mills) {
        try {
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method C : " + name);
        return "method C : " + name;
    }
}
