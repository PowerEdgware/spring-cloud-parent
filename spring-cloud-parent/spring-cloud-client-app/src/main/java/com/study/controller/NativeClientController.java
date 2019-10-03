package com.study.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RestController //TODO 方便  ClientController测试
public class NativeClientController {
	static final Logger log = LoggerFactory.getLogger(NativeClientController.class);
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	RestTemplate restTemplate;

	@Value("${target.application.name}")
	private String targetService;

	volatile Set<String> targetUrlSet = new HashSet<>();

	@Scheduled(initialDelay = 1000, fixedRate = 5 * 1000)
	public void urlUpdater() {
		Set<String> oldUrl = this.targetUrlSet;
		List<ServiceInstance> instances = discoveryClient.getInstances(targetService);
		Set<String> newUrl = instances.stream().map(s -> s.isSecure() ? "https://" + s.getHost() + ":" + s.getPort()
				: "http://" + s.getHost() + ":" + s.getPort()).collect(Collectors.toSet());

		this.targetUrlSet = newUrl;
		oldUrl.clear();// help GC
		log.info("targetService=" + targetService + " urls=" + this.targetUrlSet);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private String selectUrl() {
		List<String> targetUrl = new ArrayList<>(this.targetUrlSet);
		Random rnd = new Random();
		int index = rnd.nextInt(targetUrl.size());
		return targetUrl.get(index);
	}

	// 自定义实现，服务调用
	@GetMapping("/invoke/say")
	public String invekeSay(@RequestParam("message") String message) {
		String targetUrl = selectUrl();
		System.out.println("url=" + targetUrl);
		return restTemplate.getForObject(targetUrl + "/say?message=" + message, String.class);
	}
	
	public void readme(String serviceId) {
		discoveryClient.getInstances(serviceId).stream().findFirst().get().getUri();
	}

}
