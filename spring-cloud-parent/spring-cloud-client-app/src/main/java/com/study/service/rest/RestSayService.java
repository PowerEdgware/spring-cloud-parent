package com.study.service.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.annotation.service.invoke.RestClient;

@RestClient(name="${target.application.name}")
public interface RestSayService {

	@GetMapping("/say")//目标服务的 /say 接口
	String invokeSay(@RequestParam(name="message") String message);
}
