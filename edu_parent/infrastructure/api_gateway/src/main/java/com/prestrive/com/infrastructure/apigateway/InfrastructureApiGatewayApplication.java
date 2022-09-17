package com.prestrive.com.infrastructure.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan("com.prestrive.com")
public class InfrastructureApiGatewayApplication {

    public static void main(String[] args) {
        try{
            SpringApplication.run(InfrastructureApiGatewayApplication.class,args);
        }catch(Exception e){
            e.printStackTrace();
            log.debug("the exception is {}", e.getMessage(), e);
        }

    }


}
