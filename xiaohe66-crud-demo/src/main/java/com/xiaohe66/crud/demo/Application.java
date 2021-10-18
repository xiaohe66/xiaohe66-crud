package com.xiaohe66.crud.demo;


import com.xiaohe66.crud.EnableCrud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaohe
 * @since 2021.09.26 11:11
 */
@EnableCrud
@SpringBootApplication
@MapperScan(basePackages = "com.xiaohe66.crud.demo.mapper")
@Slf4j
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        log.info("启动完成");

    }
}
