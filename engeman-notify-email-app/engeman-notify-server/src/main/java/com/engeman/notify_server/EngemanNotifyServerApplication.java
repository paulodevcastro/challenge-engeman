package com.engeman.notify_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EngemanNotifyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EngemanNotifyServerApplication.class, args);
	}

}
