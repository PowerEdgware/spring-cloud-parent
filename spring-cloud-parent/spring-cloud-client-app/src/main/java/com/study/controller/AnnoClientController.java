package com.study.controller;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.service.feign.FeignSayService;
import com.study.service.rest.RestSayService;

/**
 * 自定义feign方式实现调用||||||||最后使用feign客户端调用并了解其原理
 *
 */
@RestController
public class AnnoClientController {
	@Autowired
	private FeignSayService feignSayService;
	@Autowired
	private RestSayService restSayService;

	@RequestMapping("/feign/say")
	public String invokeByFeign(@RequestParam(name = "message") String message) {
		System.out.println(feignSayService.getClass()+" Feign");
		return feignSayService.invokeSay(message);
	}
	
	@RequestMapping("/rest/say")
	public String invokeByRestClient(@RequestParam(name = "message") String message) {
		System.out.println(restSayService.getClass()+" restSay");
		return restSayService.invokeSay(URLEncoder.encode(message));
	}

}
