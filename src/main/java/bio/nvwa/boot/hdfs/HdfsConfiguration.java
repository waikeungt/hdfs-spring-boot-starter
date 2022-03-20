package bio.nvwa.boot.hdfs;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.time.Duration;

/**
 * 对象池自动装配
 *
 * @author waikeungt
 * @version 1.0
 */
@Configuration
public class HdfsConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(HdfsConfiguration.class);

	/**
	 * 配置内容
	 */
	private final HdfsProperties hdfsProperties;

	/**
	 * 对象池
	 */
	private HdfsPool pool;

	@Autowired
	public HdfsConfiguration(HdfsProperties hdfsProperties) {
		this.hdfsProperties = hdfsProperties;
	}

	@Bean
	@ConditionalOnClass({HdfsFactory.class})
	protected synchronized HdfsPool hdfsPool() {
		HdfsFactory hdfsFactory = new HdfsFactory(hdfsProperties);
		//设置对象池的相关参数
		GenericObjectPoolConfig<HdfsClient> poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMaxIdle(hdfsProperties.getMaxIdle());
		poolConfig.setMaxTotal(hdfsProperties.getMaxTotal());
		poolConfig.setMinIdle(hdfsProperties.getMinIdle());
		poolConfig.setBlockWhenExhausted(true);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(0X1b7740L));
		//一定要关闭jmx，不然springboot启动会报已经注册了某个jmx的错误
		poolConfig.setJmxEnabled(false);

		//新建一个对象池,传入对象工厂和配置
		pool = new HdfsPool(hdfsFactory, poolConfig);

		int size = initPool(hdfsProperties.getInitialSize(), hdfsProperties.getMaxIdle());
		LOGGER.info("初始化HDFS对象池完成，初始HdfsClient数量是" + size);
		return pool;
	}

	/**
	 * 预先加载testObject对象到对象池中
	 *
	 * @param initialSize 初始化连接数
	 * @param maxIdle     最大空闲连接数
	 */
	private int initPool(int initialSize, int maxIdle) {
		if (initialSize <= 0) {
			return 0;
		}

		int size = Math.min(initialSize, maxIdle);
		for (int i = 0; i < size; i++) {
			try {
				pool.addObject();
			} catch (Exception e) {
				LOGGER.error("初始化HDFS对象池异常", e);
				throw new RuntimeException(e);
			}
		}
		return size;
	}

	/**
	 * 销毁对象池
	 */
	@PreDestroy
	public void destroy() {
		if (pool != null) {
			pool.close();
			LOGGER.info("销毁对象池");
		}
	}
}
