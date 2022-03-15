package com.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.crowd.mapper")
@SpringBootApplication
public class MysqlCrowdMainClass {
    public static void main(String[] args) {
        SpringApplication.run(MysqlCrowdMainClass.class);
    }
}


