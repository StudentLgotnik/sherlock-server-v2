package com.sherlock.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SherlockDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SherlockDiscoveryApplication.class, args);
    }
}
