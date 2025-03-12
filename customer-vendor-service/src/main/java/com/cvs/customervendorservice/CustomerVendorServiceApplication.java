package com.cvs.customervendorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients//(basePackages = "com.cvs.customervendorservice.feign")
@EnableJpaRepositories(basePackages = "com.cvs.customervendorservice.repository")
public class CustomerVendorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerVendorServiceApplication.class, args);
    }
}
