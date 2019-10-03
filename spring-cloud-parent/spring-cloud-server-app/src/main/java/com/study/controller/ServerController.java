package com.study.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {

	@Value("${server.port}")
	int port;

	@GetMapping("/say")
	public String sayHello(@RequestParam("message") String message) {
		return "Hello," + message + " port=" + port;
	}
}
