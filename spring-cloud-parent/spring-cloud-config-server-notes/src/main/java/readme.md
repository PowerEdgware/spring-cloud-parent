
##spring-cloud-config-server

提供HTTP API用于获取外部化资源。  
基础依赖：`spring-cloud-config-server`  

```
  <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
```


注解：`@EnableConfigServer`  

#### 基于 Git 实现

版本化配置获取接口URI  

/应用名/profile/${label}

/应用名/profile/ = /应用名/profile/master

/应用名/ = /应用名.properties

${label} : 分支


#### 设计原理

##### 分析 `@EnableConfigServer`  

```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ConfigServerConfiguration.class)
public @interface EnableConfigServer {

}
```

实际配置类：`ConfigServerConfiguration`  

```
@Configuration
public class ConfigServerConfiguration {
	class Marker {}

	@Bean
	public Marker enableConfigServerMarker() {
		return new Marker();
	}
}
```
被`ConfigServerAutoConfiguration`所条件引用：  

```
@Configuration
@ConditionalOnBean(ConfigServerConfiguration.Marker.class)
@EnableConfigurationProperties(ConfigServerProperties.class)
@Import({ EnvironmentRepositoryConfiguration.class, CompositeConfiguration.class, ResourceRepositoryConfiguration.class,
		ConfigServerEncryptionConfiguration.class, ConfigServerMvcConfiguration.class })
public class ConfigServerAutoConfiguration {

}
```

##### 存储配置仓库  
`EnvironmentRepository`  

从配置`EnvironmentRepositoryConfiguration` 看到默认的实现：`EnvironmentRepositoryConfiguration`  


```
@Configuration
@ConditionalOnMissingBean(value = EnvironmentRepository.class, search = SearchStrategy.CURRENT)
class DefaultRepositoryConfiguration {
	@Autowired
	private ConfigurableEnvironment environment;

	@Autowired
	private ConfigServerProperties server;

	@Autowired(required = false)
	private TransportConfigCallback customTransportConfigCallback;

	@Bean
	public MultipleJGitEnvironmentRepository defaultEnvironmentRepository(
	        MultipleJGitEnvironmentRepositoryFactory gitEnvironmentRepositoryFactory,
			MultipleJGitEnvironmentProperties environmentProperties) throws Exception {
		return gitEnvironmentRepositoryFactory.build(environmentProperties);
	}
}
```
默认JGit实现  
由上述`ConditionalOnMissingBean`可以看到，当容器存在`EnvironmentRepository`时，将会覆盖掉默认的实现，给自定义实现提供了思路。  


##### 端点获取

`ConfigServerMvcConfiguration`中注入的Controller:`EnvironmentController`提供的配置获取对外接口  
创建：

```
	@Bean
	public EnvironmentController environmentController(EnvironmentRepository envRepository, ConfigServerProperties server) {
		EnvironmentController controller = new EnvironmentController(encrypted(envRepository, server), this.objectMapper);
		controller.setStripDocumentFromYaml(server.isStripDocumentFromYaml());
		controller.setAcceptEmpty(server.isAcceptEmpty());
		return controller;
	}
```

路径：`EnvironmentController.labelled --> @RequestMapping("/{name}/{profiles}/{label:.*}")`  

以上是服务端实现配置存储和获取配置接口流程，具体参考API即可。  




