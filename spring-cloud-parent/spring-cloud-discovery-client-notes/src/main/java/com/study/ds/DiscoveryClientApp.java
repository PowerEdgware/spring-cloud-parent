package com.study.ds;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class DiscoveryClientApp {
	
	void readme() {
		//URLConfigurationSource
	}

	public static void main(String[] args) {
		// SpringApplication.run(DiscoveryClientApp.class, args);
		new SpringApplicationBuilder(DiscoveryClientApp.class).web(WebApplicationType.SERVLET).run(args);
	}

	@RequestMapping("/")
	public String home() {
		return "Hello world";
	}

	@Autowired
	private DiscoveryClient discoveryClient;

	@PostConstruct
	private void init() {
		//class org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient
		System.out.println("Current Discovery Client:" + discoveryClient.getClass());
	}

	@GetMapping("/services")
	public List<String> queryServices() {
		return discoveryClient.getServices();
	}

	@GetMapping("/services/{instanceId}")
	public Object queryInstance(@PathVariable String instanceId) {
		return discoveryClient.getInstances(instanceId);
	}
}
