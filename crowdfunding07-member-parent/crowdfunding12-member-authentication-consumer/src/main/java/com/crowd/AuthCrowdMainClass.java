package com.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//启用feign客户端功能（  使得本module中定义的远程调用函数接口（此处为依赖api-module中的定义），使得在handler类中装配接口实例时不报错 ）
@EnableFeignClients

//@EnableEurekaClient  低版本的springboot需要写该注解
@SpringBootApplication
public class AuthCrowdMainClass {

    public static void main(String[] args) {
        SpringApplication.run(AuthCrowdMainClass.class,args);
    }
}
