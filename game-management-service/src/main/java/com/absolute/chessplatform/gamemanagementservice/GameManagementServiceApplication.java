package com.absolute.chessplatform.gamemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.absolute.chessplatform.gamemanagementservice.clients")
public class GameManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameManagementServiceApplication.class, args);
	}

}
