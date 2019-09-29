package com.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class NativeCloudApp {

	void readme() {
		// BootstrapConfiguration
		//-PropertySourceBootstrapConfiguration
		//-ConfigServiceBootstrapConfiguration
		//-ConfigServicePropertySourceLocator
		
		// ApplicationListener
		// -BootstrapApplicationListener
		// -ConfigFileApplicationListener
		//ApplicationContextInitializer
		//EventPublishingRunListener
	}

	public static void main(String[] args) {
		// 正常启动
		// SpringApplication.run(NativeCloudApp.class, args);

		// 显示设置父上下文方式启动
		AnnotationConfigApplicationContext myParent = new AnnotationConfigApplicationContext();
		myParent.setId("ALLGER");
		myParent.registerBean("helloBean", String.class, "my hello bean");
		myParent.refresh();

		new SpringApplicationBuilder(NativeCloudApp.class)
			.parent(myParent)
			.bannerMode(Mode.CONSOLE)
			.run(args);

	}

	// 从myParent 上下文注入bean
	@Autowired
	@Qualifier("helloBean")
	private String helloBean;

	@GetMapping("/helloBean")
	public String renderHelloBean() {
		return helloBean;
	}
}
