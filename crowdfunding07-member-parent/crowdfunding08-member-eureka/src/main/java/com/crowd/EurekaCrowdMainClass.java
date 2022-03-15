package com.crowd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//启动后，访问http://localhost:1000/   可查看当前eureka的注册情况


@EnableEurekaServer
@SpringBootApplication
public class EurekaCrowdMainClass {

    public static void main(String[] args) {
        SpringApplication.run(EurekaCrowdMainClass.class);
    }
}
