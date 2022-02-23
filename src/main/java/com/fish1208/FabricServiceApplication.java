package com.fish1208;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FabricServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FabricServiceApplication.class, args);
    }

}
