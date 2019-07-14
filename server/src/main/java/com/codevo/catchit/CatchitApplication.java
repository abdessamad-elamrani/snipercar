package com.codevo.catchit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CatchitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatchitApplication.class, args);
	}

}
