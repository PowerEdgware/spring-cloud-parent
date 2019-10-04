package com.study.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="${target.application.name}")//serviceName
public interface FeignSayService {

	@GetMapping("/say")
	String invokeSay(@RequestParam(name = "message")String message);
}
