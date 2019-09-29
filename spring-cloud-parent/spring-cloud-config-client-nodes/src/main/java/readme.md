## spring cloud client

依赖：`spring-cloud-config-client`  

### 从config-server拉取外部配置

client端，bootstrap.yml配置服务端uri：`spring.cloud.config.uri`  
默认是：`http://localhost:8888` 从远端拉取配置来初始化 Spring当前上下文的 `Environment`  


### 实现原理  
从spring-cloud-native可知，应用启动时 首先从`spring-cloud-context/META-INF/spring.factories`加载`BootstrapApplicationListener`  
`Environment`创建完毕会发布`ApplicationEnvironmentPreparedEvent`事件，该事件被`BootstrapApplicationListener`监听，  
在实践处理过程中，会创建BootstrapContext父上下文。简要代码如下：  

```
private ConfigurableApplicationContext bootstrapServiceContext(
			ConfigurableEnvironment environment, final SpringApplication application,
			String configName) {
			...
			// Use names and ensure unique to protect against duplicates
		List<String> names = new ArrayList<>(SpringFactoriesLoader
				.loadFactoryNames(BootstrapConfiguration.class, classLoader));
		    ...
}
```
  
上述代码会加载并创建`BootstrapConfiguration`的相关类作为BootstrapContext的`Configuration`配置类。
比如：  
`spring-cloud-context/META-INF/spring.factories` 的`PropertySourceBootstrapConfiguration`  
`spring-cloud-config-client/META-INF/spring.factories` 的`ConfigServiceBootstrapConfiguration`等  
其中`ConfigServiceBootstrapConfiguration` 会创建外部资源加载的类：`ConfigServicePropertySourceLocator` 此类  
会从远程服务端获取配置资源。  

BootstrapContext创建完毕后，会先于子Context刷新上下文，其中经过上述步骤，BootstrapConext会创建一些`ApplicationContextInitializer`  
加入到子上下文中初始化容器中，具体代码还是在`BootstrapApplicationListener`：

```
	private void apply(ConfigurableApplicationContext context,
			SpringApplication application, ConfigurableEnvironment environment) {
		@SuppressWarnings("rawtypes")
		List<ApplicationContextInitializer> initializers = getOrderedBeansOfType(context,
				ApplicationContextInitializer.class);
		application.addInitializers(initializers
				.toArray(new ApplicationContextInitializer[initializers.size()]));
		addBootstrapDecryptInitializer(application);
	}

```

其中有一个`ApplicationContextInitializer`至关重要，实现类为：`PropertySourceBootstrapConfiguration`。他会在  
子`SpringApplication.run`被调用，调用的是`PropertySourceBootstrapConfiguration.initialize`初始化方法，此方法会  
在子上下文刷新之前调用`PropertySourceLocator`去获取远程Config-Server的配置。  


  
