package com.study.controller;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.study.annotation.MockLoadBalanced;
import com.study.loadbalance.CustomRestTemplateInterceptor;

@RestController
public class ClientController {
	static Logger log = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	// bean Config
	// 方法一，使用ribbon 负载均衡
	@Bean
	@LoadBalanced
	public RestTemplate ribbonRestTemplate() {
		return new RestTemplate();
	}

	@GetMapping("/lbsay/{serviceName}")
	public String ribbonInvakeSay(@PathVariable("serviceName") String serviceName, String message) {
		log.warn("incoming serviceName=" + serviceName + " message=" + message);

		return restTemplate.getForObject("http://" + serviceName + "/say?message=" + message, String.class);
	}

	// 自定义LoadBalance
	@Autowired
	@MockLoadBalanced
	private RestTemplate customRestTemplate;

	@Bean
	@MockLoadBalanced
	public RestTemplate customRestTemplate() {
		// 添加自定义拦截器或者依赖注入
		return new RestTemplate();
	}

	// 自定义拦截器Bean
	@Bean
	public ClientHttpRequestInterceptor restTemplateInterceptor() {
		return new CustomRestTemplateInterceptor();
	}

	// 注入自定义的拦截器到RestTemplate or 使@PostConstruct

	@Bean
	@Autowired
	public Object autowired2RestTemaplate(@MockLoadBalanced Collection<RestTemplate> restTemplate,
			ClientHttpRequestInterceptor restTemplateInterceptor) {
		restTemplate.forEach(rt -> {
			rt.setInterceptors(Arrays.asList(restTemplateInterceptor));
		});

		return new Object();
	}

	@GetMapping("/mysay/{serviceName}")
	public String myLoadBalanceInvokeSay(@PathVariable("serviceName") String serviceName, String message) {
		//TODO 这里使用restTemplate自定义蓝拦截器,对中文参数进行编码
		return customRestTemplate.getForObject("/" + serviceName + "/say?message=" + URLEncoder.encode(message), String.class);
	}

}
