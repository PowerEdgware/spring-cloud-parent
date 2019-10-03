package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling//spring定时调度
@EnableDiscoveryClient//服务发现
public class ClientSideApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientSideApplication.class, args);
	}
	
	
	void readme() {
		//native ribbon config
		//CommonClientConfigKey
		
		//RibbonClientConfiguration
		
		//custom ribbonClient
		//@RibbonClient
		//@RibbonClients
		
		// Using the Ribbon API Directly
		//LoadBalancerClient 
		
		//RequestContext 
	}
}
