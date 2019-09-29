package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApp {
	
	void readme() {
		//ConfigurationClassPostProcessor
		//ConfigServerConfiguration
		//ConfigServerAutoConfiguration
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApp.class, args);
		System.out.println(System.getProperty("user.home"));
	}
}
