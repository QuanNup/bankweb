package com.example.bankapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankapiApplication.class, args);
    }

}
