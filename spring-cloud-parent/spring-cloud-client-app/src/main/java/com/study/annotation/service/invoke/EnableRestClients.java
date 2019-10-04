package com.study.annotation.service.invoke;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

//
//模拟@EnableFeignClients，实现自定义 @link @RestClient 扫描 
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RestClientRegistar.class)
public @interface EnableRestClients {

	/**
	 * 标注 {@link RestClient} 的接口
	 * 
	 * @return
	 */
	Class<?>[] clients() default {};
}
