package com.prestrive.com.eduction.service.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源配置，因为继承了mysql依赖，但这里用不到
@ComponentScan("com.prestrive.com")
@EnableDiscoveryClient//通用注册中心注解
public class ServiceOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class);
    }
}
