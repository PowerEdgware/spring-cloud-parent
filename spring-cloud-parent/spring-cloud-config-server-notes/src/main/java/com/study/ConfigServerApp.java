package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.config.EnvironmentRepositoryConfiguration;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApp {
	
	void readme() {
		//ConfigurationClassPostProcessor
		//ConfigServerConfiguration
		//ConfigServerAutoConfiguration
		//配置仓库： EnvironmentRepositoryConfiguration
		
		//BootstrapConfiguration
		//-ConfigServerBootstrapConfiguration
		
		//EnableAutoConfiguration
		//-ConfigServerAutoConfiguration
		
		//ConfigServerMvcConfiguration
		//-EnvironmentController
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApp.class, args);
		System.out.println(System.getProperty("user.home"));
	}
}
