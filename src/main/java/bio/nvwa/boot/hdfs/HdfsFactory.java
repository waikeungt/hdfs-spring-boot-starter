package bio.nvwa.boot.hdfs;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.LocalFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HDFS工厂
 * @author waikeungt
 * @version 1.0
 */
public class HdfsFactory implements PooledObjectFactory<HdfsClient> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HdfsFactory.class);

	private final HdfsProperties hdfsProperties;

	public HdfsFactory(HdfsProperties hdfsProperties) {
		this.hdfsProperties = hdfsProperties;
	}

	private static int ha = -1;

	/**
	 * 构造一个封装对象
	 *
	 * @return HdfsClient
	 * @throws Exception 异常
	 */
	@Override
	public PooledObject<HdfsClient> makeObject() throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsProperties.getDefaultFs());
		if (ha == -1) {
			ha = 0;
		}
		if (hdfsProperties.getNameservices() != null && !"".equals(hdfsProperties.getNameservices())) {
			conf.set("dfs.nameservices", hdfsProperties.getNameservices());
			conf.set("dfs.ha.namenodes." + hdfsProperties.getNameservices(), hdfsProperties.getHa());
			String[] haStr = hdfsProperties.getHa().split(",");
			String[] address = hdfsProperties.getRpcAddress().split(",");
			for (int i = 0; i < haStr.length; i++) {
				conf.set("dfs.namenode.rpc-address." + hdfsProperties.getNameservices() + "." + haStr[i], address[i]);
			}
			conf.set("dfs.client.failover.proxy.provider." + hdfsProperties.getNameservices(),
					"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
			int printed = 2;
			if (ha != printed) {
				ha = 1;
			}
		}
		conf.set("fs.hdfs.impl", HdfsClient.class.getName());
		conf.set("fs.file.impl", LocalFileSystem.class.getName());
		// 若禁用缓存
		conf.set("fs.hdfs.impl.disable.cache", "true");
		if (ha == 0) {
			LOGGER.info("HDFS使用普通连接，fs.defaultFS={}", hdfsProperties.getDefaultFs());
			ha = 2;
		} else if (ha == 1) {
			LOGGER.info("HDFS使用HA连接，fs.defaultFS={}，nameservices={}，ha={}，rpc-address={}",
					hdfsProperties.getDefaultFs(), hdfsProperties.getNameservices(),
					hdfsProperties.getHa(),hdfsProperties.getRpcAddress());
			ha = 2;
		}
		HdfsClient hdfsClient = (HdfsClient) HdfsClient.get(conf);
		LOGGER.info("初始HdfsClient={}", hdfsClient);
		return new DefaultPooledObject<>(hdfsClient);
	}

	/**
	 * 销毁对象
	 *
	 * @param p PooledObject&lt;HdfsClient&gt;
	 */
	@Override
	public void destroyObject(PooledObject<HdfsClient> p) {
		LOGGER.info("销毁对象" + p.getObject());
		p.getObject().destroy();
	}

	/**
	 * 验证对象是否可用
	 *
	 * @param p PooledObject&lt;HdfsClient&gt;
	 * @return 是否可用
	 */
	@Override
	public boolean validateObject(PooledObject<HdfsClient> p) {
		return p.getObject() != null && p.getObject().isActive();
	}

	/**
	 * 激活一个对象，使其可用
	 *
	 * @param p PooledObject&lt;HdfsClient&gt;
	 */
	@Override
	public void activateObject(PooledObject<HdfsClient> p) {
		p.getObject().setActive(true);
	}

	/**
	 * 钝化一个对象,也可以理解为反初始化
	 *
	 * @param p PooledObject&lt;HdfsClient&gt;
	 */
	@Override
	public void passivateObject(PooledObject<HdfsClient> p) {
		p.getObject().setActive(false);
	}
}
