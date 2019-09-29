package com.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
//@EnableDiscoveryClient
@RestController
public class ConfigClientApp {
	
	void readme() {
		//BootstrapApplicationListener
		//ApplicationContextInitializer
		//-AncestorInitializer
		//-ParentContextApplicationContextInitializer
		//-ContextIdApplicationContextInitializer
		//-DelegatingApplicationContextInitializer
		//-SharedMetadataReaderFactoryContextInitializer
		//-ServerPortInfoApplicationContextInitializer
		
		//@EnableDiscoveryClient
		//-EnableDiscoveryClientImportSelector
		//--AutoServiceRegistrationConfiguration
		//EnableAutoConfiguration
		//-ConfigClientAutoConfiguration
		//BootstrapConfiguration
		//-DiscoveryClientConfigServiceBootstrapConfiguration
		//-ConfigServiceBootstrapConfiguration
		//-ConfigClientProperties
		//--ConfigServicePropertySourceLocator
		//ApplicationListener
		//ApplicationEnvironmentPreparedEvent
		//PropertySourceBootstrapConfiguration
		//AutoServiceRegistrationAutoConfiguration
		//PropertySourceLocator
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApp.class, args);
	}
	
	@Autowired
	private ConfigurableEnvironment env;;
	
	@GetMapping("/getProp")
	public String remoteProp(@RequestParam("key")String key) {
		return env.getProperty(key);
	}
}
