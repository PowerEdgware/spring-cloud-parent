##spring cloud native

###bootstrap顶级上下文
** Spring Cloud 会增加 Bootstrap ApplicationContext **  
spring cloud 应用的顶级上下文，一般是应用上下文的父上下文。  

parent:ApplicationContext  

current: ApplicationContext   

current.parentId->parent.id  
parent.parentId->null  

** 上下文的启动刷新顺序 **  
子上下文必须等付上下文先启动  
parent.refresh  
child.refresh  

#### Bootstrap 启动流程   
bootstrap最先启动，可以用来加载一些资源。

` SpringApplication.run(NativeCloudApp.class, args)`  

** 类似SPI机制的，SpringFactoriesLoader加载一些初始化类**  
`SpringFactoriesLoader.loadFactoryNames(type, classLoader)`  
--ApplicationContextInitializer   
--ApplicationListener  
其中一个关键的类： `BootstrapApplicationListener`  用来监听`ApplicationEnvironmentPreparedEvent`  
接着调用`BootstrapApplicationListener`的事件监听方法
此监听方法会创建bootstrapContext 并且向`SpringApplication`中添加`AncestorInitializer`并把bootstrapContext传递过去  
以便后续`ApplicationContextInitializer`调用时，利用初始化做一些事情，  
比如利用`ParentContextApplicationContextInitializer`设置childContext的父context为：bootstrapContext

** ConfigFileApplicationListener boot中查找classpath下application.yml等文件的类**  


** ConfigClient 用到的 `ApplicationContextInitializer` **  
`PropertySourceBootstrapConfiguration` 用来定位远程配置中心配置文件的入口  
具体定位资源文件的类： `ConfigServicePropertySourceLocator`



  




