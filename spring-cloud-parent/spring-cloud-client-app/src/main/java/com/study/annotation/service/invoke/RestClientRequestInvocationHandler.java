package com.study.annotation.service.invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

public class RestClientRequestInvocationHandler implements InvocationHandler {

	private final String serviceName;// 被调用放服务名称
	private final BeanFactory beanFactory;
	private RestTemplate customRestTemplate;// 自定义的带有拦截器的restTemplate

	private RestClientRequestInvocationHandler(String serviceName, BeanFactory beanFactory) {
		this.serviceName = serviceName;
		this.beanFactory = beanFactory;
		this.customRestTemplate = beanFactory.getBean("customRestTemplate", RestTemplate.class);
	}

	public static RestClientRequestInvocationHandler create(String serviceName, BeanFactory beanFactory) {
		return new RestClientRequestInvocationHandler(serviceName, beanFactory);
	}

	/***
	 * 处理服务调用
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 获取方法注解
		GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
		if (getMapping == null) {
			return null;
		}
		String invokedURI = getMapping.value()[0];// 简单起见
		// 获取方法参数注解并组装服务调用方法参数
		Parameter[] parameters = method.getParameters();
		StringBuilder urlBuilder = new StringBuilder("/").append(serviceName).append(invokedURI);

		StringBuilder queryStringBuilder = new StringBuilder();
		for (int i = 0; i < parameters.length; i++) {
			RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
			if (requestParam != null) {
				String paramName = requestParam.name();
				// 获取方法参数的值
				String paramValue = String.valueOf(args[i]);
				// param1=xxx&param2=yyy
				// 拼接参数对
				queryStringBuilder.append(paramName).append("=").append(paramValue).append("&");
			} else {
				// TODO 如果为空则使用方法参数发现接口：ParameterNameDiscoverer
			}
			// 拼装完整参数
			String queryString = queryStringBuilder.toString();
			if (!StringUtils.isBlank(queryString)) {
				urlBuilder.append("?").append(queryString);
			}
			// http://$serviceName/say?message=xxx
			String url = urlBuilder.toString();
			System.out.println("URL=" + url);

			// 服务调用
			return this.customRestTemplate.getForObject(url, method.getReturnType());
		}
		return null;
	}

}
