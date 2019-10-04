package com.study.annotation.service.invoke;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.type.AnnotationMetadata;

/*
 * @link EnableRestClients
 */

public class RestClientRegistar implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {

	// BeanFactory ApplicationContext SingletonBeanRegistry
	private Environment environment;
	private BeanFactory beanFactory;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		// 获取注解@EnableRestCLients
		Map<String, Object> annos = importingClassMetadata.getAnnotationAttributes(EnableRestClients.class.getName());
		// 简单起见，只解析使用@EnableRestClients标注了 @RestClient 出现在clients属性中的接口
		Class<?>[] restClientClasses = (Class<?>[]) annos.get("clients");
		// 过滤标注了@RestClient接口。

		ClassLoader clazzLoader = importingClassMetadata.getClass().getClassLoader();

		Stream.of(restClientClasses).filter(Class::isInterface)
				.filter(clazz -> clazz.isAnnotationPresent(RestClient.class)).forEach(clazz -> {
					RestClient restClient = AnnotationUtils.findAnnotation(clazz, RestClient.class);
					String serviceName = restClient.name();
					String realServiceName = environment.resolvePlaceholders(serviceName);
					// 创建代理对象
					Object proxy = Proxy.newProxyInstance(clazzLoader, new Class[] { clazz },
							RestClientRequestInvocationHandler.create(realServiceName, beanFactory));

					// 创建BeanDefinition
					BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
							.genericBeanDefinition(RestClientFactoryBean.class);
					// 通过构造参数注入属性
					beanDefinitionBuilder.addConstructorArgValue(proxy);
					beanDefinitionBuilder.addConstructorArgValue(clazz);

					AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

					// 注入BeanDefinition
					String beanName = "RestClient." + realServiceName;
					registry.registerBeanDefinition(beanName, beanDefinition);

				});
	}

	// 包装@RestClient接口代理类的BeanFactory
	static class RestClientFactoryBean implements FactoryBean<Object> {

		private Object proxy;
		private Class restClientClass;

		public RestClientFactoryBean(Object proxy, Class restClientClass) {
			this.proxy = proxy;
			this.restClientClass = restClientClass;
		}

		@Override
		public Object getObject() throws Exception {
			return proxy;
		}

		@Override
		public Class<?> getObjectType() {
			return restClientClass;
		}

	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
