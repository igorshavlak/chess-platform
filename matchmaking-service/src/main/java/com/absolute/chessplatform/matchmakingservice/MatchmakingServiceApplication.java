package com.absolute.chessplatform.matchmakingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.absolute.chessplatform.matchmakingservice.clients")
public class MatchmakingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchmakingServiceApplication.class, args);
    }

}
