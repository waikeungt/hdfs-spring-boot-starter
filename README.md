# <p align="center">项目文档</p>

![springboot](https://img.shields.io/badge/2.X-spring%20boot-green?logo=springboot)
![hdfs](https://img.shields.io/badge/3.1.2-hdfs-orange?logo=Apache%20Hadoop)
![gradle](https://img.shields.io/badge/7.2-gradle-yellow?logo=gradle)
![license](https://img.shields.io/badge/license-Apache%202.0-blue)

hdfs-spring-boot-starter可使spring-boot项目更快速地使用hdfs。
其实原来spring-boot是有hadoop的starter的，但是后面不维护了，而且对于方法的使用自己也不熟，所以用了平时使用hdfs的方式，创建了这个starter。

## 环境
本项目通过以下环境进行开发、测试和生产
- centos，deepin，alpine
- windows 10，windows 11
- oracle jdk 1.8，openjdk 1.8
- hadoop 2.7， hadoop 3.2.0
- spring boot 2.3.1
- gradle、maven
- docker（openjdk:8u322-jre-slim-buster）

## 配置
### 配置文件
在你的application.yaml文件添加下面内容
```yaml
hadoop:
  hdfs:
    # 对象池最大空闲
    maxIdle: 5
    # 对象池最大总数
    maxTotal: 20
    # 对象池最小空闲
     minIdle: 2
    # 对象池初始化连接数
    initialSize: 3
    # hdfs defaultFs
    defaultFs: hdfs://localhost:9820/ #hdfs://nameservices
    # hdfs HA设置，非HA可不写
    #nameservices: nameservices
    # hdfs HA设置，非HA可不写
    #ha: namenode1,namenode2
    # hdfs HA设置，非HA可不写
    #rpcAddress: 172.0.0.1:8020,172.0.0.2:8020
```

### 参数
可以通过启动参数将配置项注入

### 代码设置
可以在启动方法将配置内容通过System.setProperty将配置项注入
```java
@ComponentScan(basePackages = {"bio.nvwa.boot.hdfs"})
public class HdfsSpringBootStarterApplication {

	public static void main(String[] args) {
		System.setProperty("hadoop.hdfs.maxIdle", "10");
		System.setProperty("hadoop.hdfs.maxTotal", "20");
		System.setProperty("hadoop.hdfs.minIdle", "2");
		System.setProperty("hadoop.hdfs.initialSize", "2");
		System.setProperty("hadoop.hdfs.maxWaitMillis", "3000");
		System.setProperty("hadoop.hdfs.defaultFs", "hdfs://localhost:9820");
		SpringApplication.run(HdfsSpringBootStarterApplication.class, args);
	}
}
```

## 代码调用
启动的时候spring boot会将操作hdfs的类（HdfsService）注入，你可以将其当成你代码里的service使用就ok了。

```java
package bio.nvwa.boot.hdfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestService.class);
	
    private final HdfsService hdfsService;

    public TestService(HdfsService hdfsService) {
	    LOGGER.info("TestService准备构造注入");
        this.hdfsService = hdfsService;
	    LOGGER.info("TestService注入HdfsService成功");
    }

    public void test() {
	    LOGGER.info("hdfs测试开始");
        hdfsService.test();
        LOGGER.info("hdfs测试完成");
    }
}
```

## 引入
https://mvnrepository.com/artifact/bio.nvwa.boot/hdfs-spring-boot-starter或者到中央仓库查询最新版

### maven方式引入
```xml
<dependency>
    <groupId>bio.nvwa.boot</groupId>
    <artifactId>hdfs-spring-boot-starter</artifactId>
    <version>版本号</version>
</dependency>
```

### gradle方式引入
```groovy
dependencies {
    implementation 'bio.nvwa.boot:hdfs-spring-boot-starter:版本号'
}
```